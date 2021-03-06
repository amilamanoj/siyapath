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

package org.siyapath.job;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.siyapath.NodeContext;
import org.siyapath.NodeInfo;
import org.siyapath.SiyapathConstants;
import org.siyapath.service.Job;
import org.siyapath.service.Result;
import org.siyapath.service.Siyapath;
import org.siyapath.service.TaskStatus;
import org.siyapath.utils.CommonUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class BackupHandler {

    private static final Log log = LogFactory.getLog(BackupHandler.class);

    private NodeContext context;
    private Job jobToBackup;
    private Map<Integer, ProcessingTask> taskMap;
    private NodeInfo nodeToBackup;
    private StatusThread statusThread;

    public BackupHandler(NodeContext nodeContext) {
        this.context = nodeContext;
    }

    /**
     * Invoked when a node requests from this node to be its backup
     *
     * @param job          Job to backup
     * @param nodeToBackup Node that has to be backed-up
     * @return true if request is accepted, false otherwise
     */
    public boolean requestBecomeBackup(Job job, NodeInfo nodeToBackup) {
        if (context.isBackup() || !context.getNodeResource().isIdle()) {
            return false;
        }
        context.setBackup(true);
        this.jobToBackup = job;
        this.nodeToBackup = nodeToBackup;
        this.taskMap = new HashMap<Integer, ProcessingTask>();
        statusThread = new StatusThread();
        statusThread.start();

        return true;
    }

    /**
     * Receives result of completed task from job processor and saves it
     *
     * @param result Completed task result
     */
    public void updateTaskResult(Result result) {
        ProcessingTask task = taskMap.get(result.getTaskID());
        if (task == null) {
            task = new ProcessingTask(result.getJobID(), result.getTaskID(),
                    jobToBackup.getReplicaCount(), jobToBackup.getTasks().get(result.getTaskID()));
        }
        task.addResult(result.getResults());
        task.incrementResultReceivedCount();
        ProcessingTask.TaskReplica taskReplica = task.new TaskReplica(TaskStatus.COMPLETED);
        taskReplica.setProcessingNode(CommonUtils.deSerialize(result.getProcessingNode()));
        task.addToTaskReplicaList(taskReplica);
        taskMap.put(result.getTaskID(), task);
        if (task.getResultReceivedCount() >= jobToBackup.getReplicaCount()) {
            validateResults(result.getTaskID());
        }
    }

    /**
     * Thread that polls the job processor to see if it is alive
     */
    private class StatusThread extends Thread {
        private boolean isRunning = true;

        @Override
        public void run() {
            while (isRunning) {
                boolean isAlive = isNodeAlive();
                if (!isAlive) {
                    log.warn("Job Distributor is no longer available: " + nodeToBackup);
                    context.getJobProcessor().addTasksToTaskMap(taskMap);
                    context.getJobProcessor().addNewJob(jobToBackup);
                    endBackup();
                }
                try {
                    Thread.sleep(SiyapathConstants.BACKUP_STATUS_CHECK_INTERVAL);
                } catch (InterruptedException e) {
                    log.warn(e.getMessage());
                }
            }
        }

        public void stopThread() {
            isRunning = false;
        }

        private boolean isNodeAlive() {
            TTransport transport = new TSocket(nodeToBackup.getIp(), nodeToBackup.getPort());
            try {
                transport.open();
                TProtocol protocol = new TBinaryProtocol(transport);
                Siyapath.Client client = new Siyapath.Client(protocol);
                return client.isAlive();
            } catch (TException e) {
                log.error("Error contacting job processor to check liveness" + e.getMessage());
            } finally {
                transport.close();
            }
            return false;
        }
    }

    /**
     * Stops being the backup. Invoked when the job is complete.
     */
    public void endBackup() {
        statusThread.stopThread();
        taskMap = null;
        jobToBackup = null;
        nodeToBackup = null;
        context.setBackup(false);
    }

    /**
     * Validates results comparing results from replicas
     *
     * @param taskId ID of the task
     * @return true if all results of replicas are equal, false otherwise
     */
    public synchronized void validateResults(int taskId) {
        ProcessingTask pTask = taskMap.get(taskId);
        ArrayList<byte[]> resultList = pTask.getResultList();
        boolean isValid = false;

        if (!resultList.isEmpty()) {
            byte[] firstResult = resultList.get(0);
            for (byte[] resultArray : resultList) {
                isValid = Arrays.equals(firstResult, resultArray);
                if (!isValid) {
                    break;
                }
            }
            if (isValid) {
                pTask.setValidatedResult(firstResult);
                taskMap.put(taskId, pTask);
                log.info("Result validated for Task:" + taskId);
            } else {
                log.info("Result validation failed, removing task from task map. Task:" + taskId);
                taskMap.remove(taskId);
            }
        }
    }
}
