/*
 * Distributed under the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.siyapath.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.siyapath.NodeContext;
import org.siyapath.NodeInfo;
import org.siyapath.SiyapathConstants;
import org.siyapath.service.*;
import org.siyapath.task.SiyapathTask;
import org.siyapath.utils.CommonUtils;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages job submission to the volunteer computing network
 */
public class UserHandler {
    private static final Log log = LogFactory.getLog(UserHandler.class);

    private NodeContext context;
    private Map<Integer, JobData> jobMap = new HashMap<Integer, JobData>();

    /**
     * Create a UserHandler with a random port and id
     */
    public UserHandler() {
        this(new NodeInfo());
    }

    /**
     * Creates a UserHandler with a given ip, port and id
     *
     * @param nodeInfo NodeInfo with the specified data
     */
    public UserHandler(NodeInfo nodeInfo) {
        this.context = new NodeContext(nodeInfo);
    }

    public JobData getJobData(int jobId) {
        return jobMap.get(jobId);
    }

    /**
     * Creates a unique jobID by appending ip and timestamp and a random number
     *
     * @return the created jobID
     */
    private String generateJobIDString() {
        String ip = context.getNodeInfo().getIp();
        String timestamp = new SimpleDateFormat("yyyy.MM.dd_HH:mm:ss.S").format(new Date());
        int random = CommonUtils.getRandomNumber(1000);
        String jobIdString = ip + "::" + timestamp + "::" + random;
        return jobIdString;
    }

    /**
     * @param username user name
     * @param password password
     * @return String success if user can be authenticated, failure otherwise, or exception string
     */
    public String authenticate(String username, String password) {
        String res = null;
        TTransport transport = new TSocket(CommonUtils.getBootstrapperIP(), CommonUtils.getBootstrapperPort());
        try {
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            Siyapath.Client client = new Siyapath.Client(protocol);
            res = client.userLogin(username, password);
        } catch (TTransportException e) {
            if (e.getCause() instanceof ConnectException) {
                res = "connecEx";
                log.error(e.getMessage());
            }
        } catch (TException e) {
            res = "tEx";
            e.printStackTrace();
        } finally {
            transport.close();
        }
        return res;
    }

    /**
     * Selects a distributor nodes and sends the job
     *
     * @param name name of the job
     * @param job  job to be sub
     * @return jobID if success. -1 if failed
     * @throws SubmissionFailedException
     */
    public int submitJob(String name, Job job) throws SubmissionFailedException {
        NodeInfo selectedNode = null;
        try {
            selectedNode = getDistributorNode();
            if (selectedNode == null) {
                throw new SubmissionFailedException("Could not select a job processor node", null);
            }
            JobData jobData = new JobData(job.getJobID(), name, job, selectedNode);
            boolean isSubmitted = sendJob(job, selectedNode);
            jobMap.put(jobData.getId(), jobData);
            if (isSubmitted) {
                new Thread(new BackupNodePoller(job.getJobID())).start();
                return job.getJobID();
            }
        } catch (TException e) {
            throw new SubmissionFailedException("Could not submit the job", e);
        }
        return -1;
    }

    /**
     * Creates a job object for a given list of tasks
     *
     * @param taskList     task data to be used for the job
     * @param replicaCount number of replicas to run a task on
     * @return a new job
     * @throws IOException
     */
    public Job createJob(Map<String, TaskData> taskList, int replicaCount) throws IOException {
        String jobIdString = this.generateJobIDString();
        int jobId = Math.abs(jobIdString.hashCode());
        int taskCounter = 0;
        Map<Integer, Task> taskMap = new HashMap<Integer, Task>();

        for (TaskData taskData : taskList.values()) {
            int taskId = Math.abs((jobIdString + "::" + taskCounter++).hashCode());
            Task task = createTask(jobId, taskId, taskData.getClassFile(), taskData.getInputData(), taskData.getRequiredResourceLevel(), replicaCount);
            taskMap.put(taskId, task);
        }

        return new Job(jobId, CommonUtils.serialize(context.getNodeInfo()), taskMap, replicaCount);
    }

    /**
     * Creates and adds a new task to the job given a task class
     *
     * @param taskProgramFile class for the task to be created
     * @param inputData       input data
     */
    private Task createTask(int jobId, int taskId, File taskProgramFile, byte[] inputData,
                            String requiredResources, int replicaCount) throws IOException {
        Task task = new Task(taskId, jobId, CommonUtils.convertFileToByteBuffer
                (taskProgramFile.getAbsolutePath()), ByteBuffer.wrap(inputData), getJobInterfaceName(),
                CommonUtils.serialize(context.getNodeInfo()), null, requiredResources, replicaCount);
        return task;

    }

    /**
     * Selects a volunteer node that will act as the job distributor
     *
     * @return Node information of the selected node
     */
    private NodeInfo getDistributorNode() throws TException {

        NodeInfo selectedMember = null;
        TTransport transport = new TSocket(CommonUtils.getBootstrapperIP(), CommonUtils.getBootstrapperPort());
        try {
            log.info("Getting list of members from bootstrap");
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            Siyapath.Client client = new Siyapath.Client(protocol);
            context.updateMemberSet(CommonUtils.deSerialize(client.getMembers()));
            log.info("Number of members from bootstrapper: " + context.getMemberCount());
        } finally {
            transport.close();
        }
        /*for (NodeInfo nodeInfo : context.getMemberSet()) {
            if (nodeInfo.isIdle() || nodeInfo.getNodeStatus() == NodeStatus.DISTRIBUTING) {
                selectedMember = nodeInfo;
                break;
            }
        }*/

        selectedMember = context.getRandomMember();
        log.info("Selected node: " + selectedMember);

        return selectedMember;
    }

    /**
     * Sends the job to specified node
     *
     * @param node destination node
     */
    private boolean sendJob(Job job, NodeInfo node) throws TException {
        TTransport transport = new TSocket(node.getIp(), node.getPort());
        log.info("Sending new job to node: " + node);
        try {
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            Siyapath.Client client = new Siyapath.Client(protocol);
            boolean submitted = client.submitJob(job); //TODO: Handle the boolean return (if submission isn't possible)
            return submitted;
        } finally {
            transport.close();
        }

    }


    private String getJobInterfaceName() {
        return SiyapathTask.class.getName();
    }


    public void getJobResults(int jobId) throws TException {
        JobData jobData = jobMap.get(jobId);
        NodeInfo jobHandler = jobData.getDistributorNode();

        TTransport transport = new TSocket(jobHandler.getIp(),
                jobHandler.getPort());

        try {
            log.info("Retrieving results of job: " + jobData.getName());
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            Siyapath.Client client = new Siyapath.Client(protocol);
            client.getJobResult(jobId);    // TODO: display results
        } finally {
            transport.close();
        }
    }

    /**
     * Contacts back the selected JobProcessor to get job status
     *
     * @param jobID
     */
    public synchronized Map<Integer, TaskResult> pollStatusFromJobProcessor(int jobID) throws TException {
        if (jobMap.get(jobID).getDistributorFailureCount() > SiyapathConstants.JOB_DISTRIBUTOR_FAILURE_LIMIT
                && jobMap.get(jobID).getBackupNode() != null) {
            return pollStatusFromBackup(jobID);
        } else {
            NodeInfo jobHandler = jobMap.get(jobID).getDistributorNode();
            TTransport transport = new TSocket(jobHandler.getIp(),
                    jobHandler.getPort());
            Map<Integer, TaskResult> taskCompletionMap = null;
            try {
                log.info("Polling status of job: " + jobID);
                transport.open();
                TProtocol protocol = new TBinaryProtocol(transport);
                Siyapath.Client client = new Siyapath.Client(protocol);
                //gets the map of task statuses from JobProcessor
                //Maps each taskId to its processing status <Integer,String>
                taskCompletionMap = client.getJobStatus(jobID);
            } catch (TTransportException e) {
                log.warn("Cannot connect to job distributor. JobID: " + jobID + "Node: " + jobHandler);
                jobMap.get(jobID).incrementDistributorFailureCount();
            } finally {
                transport.close();
            }
            return taskCompletionMap;
        }
    }

    private synchronized Map<Integer, TaskResult> pollStatusFromBackup(int jobID) throws TException {
        if (!jobMap.get(jobID).isPollingFromBackup()) {
            log.info("Job distributor unavailable. Polling from backup.");
            try {
                Thread.sleep(SiyapathConstants.BACKUP_STATUS_CHECK_INTERVAL);
                jobMap.get(jobID).setPollingFromBackup(true);
            } catch (InterruptedException e) {
                log.warn(e.getMessage());
            }
        }
        NodeInfo backupNode = jobMap.get(jobID).getBackupNode();
        TTransport transport = new TSocket(backupNode.getIp(), backupNode.getPort());
        Map<Integer, TaskResult> taskCompletionMap = null;
        try {
            log.info("Polling status of job from backup: " + jobID);
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            Siyapath.Client client = new Siyapath.Client(protocol);
            //gets the map of task statuses from JobProcessor
            //Maps each taskId to its processing status <Integer,String>
            taskCompletionMap = client.getJobStatus(jobID);
        } catch (TTransportException e) {
            log.warn("Cannot connect to backup node. JobID: " + jobID + "Node: " + backupNode);
            jobMap.get(jobID).incrementDistributorFailureCount();
        } finally {
            transport.close();
        }
        return taskCompletionMap;
    }

    /**
     * @param taskResultMap
     * @return true if all task statuses are marked DONE, false otherwise, at a given point of time
     */
    public boolean assessJobStatusFromTaskStatuses(Map<Integer, TaskResult> taskResultMap) {

        boolean statusCondition = true;
        boolean eachTaskStatus;
        for (TaskResult taskResult : taskResultMap.values()) {
            if (taskResult.getStatus() == TaskStatus.COMPLETED) {
                eachTaskStatus = true;
            } else {
                eachTaskStatus = false;
            }
            if (statusCondition && eachTaskStatus) statusCondition = true;
            else statusCondition = false;
        }
        return statusCondition;
    }

    private class BackupNodePoller implements Runnable {

        private int jobID;

        private BackupNodePoller(int jobID) {
            this.jobID = jobID;
        }

        @Override
        public void run() {
            NodeInfo backupNode = null;
            for (int i = 0; backupNode == null && i < 5; i++) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    log.warn(e.getMessage());
                }
                backupNode = getBackup();
            }
            jobMap.get(jobID).setBackupNode(backupNode);
        }

        private NodeInfo getBackup() {
            NodeInfo jobHandler = jobMap.get(jobID).getDistributorNode();
            NodeInfo backupNode = null;
            TTransport transport = new TSocket(jobHandler.getIp(), jobHandler.getPort());
            try {
                log.info("Getting the backup node of job: " + jobID);
                transport.open();
                TProtocol protocol = new TBinaryProtocol(transport);
                Siyapath.Client client = new Siyapath.Client(protocol);
                NodeData reply = client.getBackupNode(jobID);
                backupNode = (reply == null ? null : CommonUtils.deSerialize(reply));
            } catch (TTransportException e) {
                log.warn("Cannot get backup: " + e.getMessage());
            } catch (TException e) {
                log.warn(e.getMessage());
            } finally {
                transport.close();
            }
            return backupNode;
        }
    }

}


