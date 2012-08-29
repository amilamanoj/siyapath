package org.siyapath.job;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.siyapath.NodeInfo;
import org.siyapath.service.Task;
import org.siyapath.service.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Meta class for holding properties of a task
 */
class ProcessingTask {

    private static final Log log = LogFactory.getLog(ProcessingTask.class);

    private int jobID;
    private int taskID;
    private Task task;

    private long timeLastUpdated;
    private NodeInfo backupNode;

    private HashMap<Integer, TaskStatus> taskStatusMap;
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

        this.resultList = new ArrayList<byte[]>();
        this.taskStatusMap = new HashMap<Integer, TaskStatus>();     // maps processing node of task, to task status
        validatedResult = new byte[1];

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

    /**
     *
     * @param result
     * @return true if result was added to result list
     */
    public boolean addResult(byte[] result) {
        return this.resultList.add(result);
    }

    /**
     *
     * @return ResultList
     */
    public ArrayList<byte[]> getResultList() {
        return this.resultList;
    }

    /**
     *
     * @return taskStatusMap
     */
    public Map<Integer, TaskStatus> getTaskStatusMap() {
        return taskStatusMap;
    }

    /**
     * Adds status of task to taskStatusMap, mapping processing node ID to task's status
     *
     * @param processingNodeID
     * @param status
     */
    public void addToStatusMap(Integer processingNodeID, TaskStatus status) {
        taskStatusMap.put(processingNodeID, status);
    }

    /**
     *
     * @return validated result of a task
     */
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
     * @return timeLastUpdated
     */
    public long getTimeLastUpdated() {
        return timeLastUpdated;
    }

    /**
     *
     * @param timeLastUpdated
     */
    public void setTimeLastUpdated(long timeLastUpdated) {
        this.timeLastUpdated = timeLastUpdated;
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

}
