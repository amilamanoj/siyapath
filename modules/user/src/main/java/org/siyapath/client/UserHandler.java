package org.siyapath.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.siyapath.*;
import org.siyapath.service.*;
import org.siyapath.task.SiyapathTask;
import org.siyapath.utils.CommonUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.*;

public class UserHandler {
    private static final Log log = LogFactory.getLog(UserHandler.class);

    private NodeContext context;
    private NodeInfo clientEnd;
    private Map<Integer, JobData> jobMap = new HashMap<Integer, JobData>();


    public UserHandler() {
        this.clientEnd = new NodeInfo();
        this.context = new NodeContext(clientEnd);
    }

    public JobData getJobData(int jobId) {
        return jobMap.get(jobId);
    }

    /**
     * Creates a unique jobID by appending ip and timestamp and a random number
     *
     * @return the created jobID
     */
    public String generateJobIDString() {
        String ip = context.getNodeInfo().getIp();
        String timestamp = new SimpleDateFormat("yyyy.MM.dd_HH:mm:ss.S").format(new Date());
        int random = CommonUtils.getRandomNumber(1000);
        String jobIdString = ip + "::" + timestamp + "::" + random;
        return jobIdString;
    }

    /**
     * @param username
     * @param password
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

//    public void addJob(Map<String, TaskData> taskList){
//        int jobId = this.generateJobID();
//    }

    /**
     * Prepares the new job, selects a distributor nodes and sends the job
     *
     * @param job job
     */
    public int submitJob(String name, Job job) throws SubmissionFailedException {
        NodeInfo selectedNode = null;
        try {
            selectedNode = getDistributorNode();
            if (selectedNode == null) {
                throw new SubmissionFailedException("Could not select a job processor node", null);
            }
            JobData jobData = new JobData(job.getJobID(), name, job, selectedNode);
            sendJob(job, selectedNode);
            jobMap.put(jobData.getId(), jobData);
        } catch (TException e) {
            e.printStackTrace();
            throw new SubmissionFailedException("Could not submit the job", e);
        }
        return job.getJobID();
    }

    Job createJob(Map<String, TaskData> taskList) throws IOException {
        String jobIdString = this.generateJobIDString();
        int jobId = Math.abs(jobIdString.hashCode());
        int taskCounter = 0;
        Map<Integer, Task> taskMap = new HashMap<Integer, Task>();

        for (TaskData taskData : taskList.values()) {
            int taskId = Math.abs((jobIdString + "::" + taskCounter++).hashCode());
            Task task = createTask(jobId, taskId, taskData.getClassFile(), taskData.getInputData(), taskData.getRequiredResources());
            taskMap.put(taskId, task);
        }

        return new Job(jobId, CommonUtils.serialize(context.getNodeInfo()), taskMap);
    }

    /**
     * Creates and adds a new task to the job given a task class
     *
     * @param taskProgramFile class for the task to be created
     * @param inputData       input data
     */
    private Task createTask(int jobId, int taskId, File taskProgramFile, byte[] inputData,
                            String requiredResources) throws IOException {
        Task task = new Task(taskId, jobId, CommonUtils.convertFileToByteBuffer
                (taskProgramFile.getAbsolutePath()), ByteBuffer.wrap(inputData), getJobInterfaceName(),
                CommonUtils.serialize(context.getNodeInfo()), null, requiredResources);
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
//        } catch (TTransportException e) {
//            if (e.getCause() instanceof ConnectException) {
//                e.printStackTrace();
//            }
//        } catch (TException e) {
//            e.printStackTrace();
        } finally {
            transport.close();
        }

        for (NodeInfo nodeInfo : context.getMemberSet()) {
            if (nodeInfo.isIdle() || nodeInfo.getNodeStatus() == NodeStatus.DISTRIBUTING) {
                selectedMember = nodeInfo;
                break;
            }
        }
        log.info("Selected node: " + selectedMember);


        return selectedMember;
    }

    /**
     * Sends the job to specified node
     *
     * @param node destination node
     */
    private void sendJob(Job job, NodeInfo node) throws TException {
        TTransport transport = new TSocket(node.getIp(), node.getPort());
        log.info("Sending new job to node: " + node);
        try {
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            Siyapath.Client client = new Siyapath.Client(protocol);
            client.submitJob(job); //TODO: Handle the boolean return (if submission isn't possible)
//        } catch (TTransportException e) {
//            if (e.getCause() instanceof ConnectException) {
//                e.printStackTrace();
//            }
//        } catch (TException e) {
//            e.printStackTrace();
        } finally {
            transport.close();
        }
    }


    private String getJobInterfaceName() {
        return SiyapathTask.class.getName();
    }


    public void getJobResults(int jobId) {
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

        } catch (TTransportException e) {
            if (e.getCause() instanceof ConnectException) {
                e.printStackTrace();
            }
        } catch (TException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            transport.close();
        }
    }

    /**
     * Contacts back the selected JobProcessor to get job status
     *
     * @param jobID
     */
    public Map<Integer, TaskResult> pollStatusFromJobProcessor(int jobID) {

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
            //sets vectors to be fed to Status UI


        } catch (TTransportException e) {
            if (e.getCause() instanceof ConnectException) {
                e.printStackTrace();
            }
        } catch (TException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            transport.close();
        }

        return taskCompletionMap;
    }

    public boolean assessJobStatusFromTaskStatuses(Map<Integer, String> statusMap) {

        boolean statusCondition = true;
        boolean eachTaskStatus;

        for (String taskStatus : statusMap.values()) {
            if (taskStatus.equalsIgnoreCase("DONE")) {
                eachTaskStatus = true;
            } else {
                eachTaskStatus = false;
            }

            if (statusCondition && eachTaskStatus) statusCondition = true;
            else statusCondition = false;
        }
        return statusCondition;
    }

}


