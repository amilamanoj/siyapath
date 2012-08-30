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
import org.siyapath.NodeInfo;
import org.siyapath.service.Task;
import org.siyapath.service.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Meta class for holding properties of a task
 */
class ProcessingTask {

    private static final Log log = LogFactory.getLog(ProcessingTask.class);

    private int jobID;
    private int taskID;
    private Task task;

    private NodeInfo backupNode;

    private ArrayList<TaskReplica> taskReplicaList;
    private ArrayList<byte[]> resultList;

    private byte[] validatedResult;
    private int resultReceivedCount = 0;
    private int replicaCount;

    /**
     *
     * @param jobID
     * @param taskID
     * @param replicaCount
     * @param task
     */
    public ProcessingTask(int jobID, int taskID, int replicaCount, Task task) {
        this.jobID = jobID;
        this.taskID = taskID;
        this.replicaCount = replicaCount;
        this.task = task;

        this.taskReplicaList = new ArrayList<TaskReplica>(replicaCount);
        this.resultList = new ArrayList<byte[]>(replicaCount);
        validatedResult = new byte[10];

    }

    /**
     *
     * @return number of results received from task processors
     */
    public int getResultReceivedCount() {
        return resultReceivedCount;
    }

    /**
     * Increments resultReceivedCount as each replicated task processor sends a result
     * of the same task to job processor who dispatched the task
     */
    public void incrementResultReceivedCount() {
        this.resultReceivedCount++;
    }

    /**
     *
     * @return job ID to which the task belongs to
     */
    public int getJobID() {
        return jobID;
    }

    /**
     *
     * @return taskID
     */
    public int getTaskID() {
        return taskID;
    }

    /**
     *
     * @return Task
     */
    public Task getTask() {
        return task;
    }

    public ArrayList<TaskReplica> getTaskReplicaList() {
        return taskReplicaList;
    }

    public boolean addToTaskReplicaList(TaskReplica taskReplica) {
        return taskReplicaList.add(taskReplica);
    }

    public TaskReplica setTaskReplica(int index, TaskReplica taskReplica) {
        return taskReplicaList.set(index, taskReplica);
    }

    public boolean addResult(byte[] result) {
        return this.resultList.add(result);
    }

    public ArrayList<byte[]> getResultList() {
        return this.resultList;
    }

    public byte[] getValidatedResult() {
        return validatedResult;
    }

    /**
     *
     * @param validatedResult
     */
    public void setValidatedResult(byte[] validatedResult) {
        this.validatedResult = validatedResult;
    }

    /**
     *
     * @return backupNode
     */
    public NodeInfo getBackupNode() {
        return backupNode;
    }

    /**
     *
     * @param backupNode
     */
    public void setBackupNode(NodeInfo backupNode) {
        this.backupNode = backupNode;
    }

    /**
     *
     * @return number of replicas this task should run on
     */
    public int getReplicaCount() {
        return replicaCount;
    }

    /**
     * sets number of replicas this task should run on
     *
     * @param replicaCount
     */
    public void setReplicaCount(int replicaCount) {
        this.replicaCount = replicaCount;
    }

    class TaskReplica {
        private NodeInfo processingNode;
        private TaskStatus taskStatus;
        private long timeLastUpdated;

        TaskReplica(TaskStatus taskStatus) {
            this.taskStatus = taskStatus;
        }

        public NodeInfo getProcessingNode() {
            return processingNode;
        }

        public void setProcessingNode(NodeInfo processingNode) {
            this.processingNode = processingNode;
        }

        public TaskStatus getTaskStatus() {
            return taskStatus;
        }

        public void setTaskStatus(TaskStatus taskStatus) {
            this.taskStatus = taskStatus;
        }

        public long getTimeLastUpdated() {
            return timeLastUpdated;
        }

        public void setTimeLastUpdated(long timeLastUpdated) {
            this.timeLastUpdated = timeLastUpdated;
        }
    }
}
