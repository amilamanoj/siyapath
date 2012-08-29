package org.siyapath.job;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.siyapath.NodeInfo;
import org.siyapath.service.Task;
import org.siyapath.service.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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


    public ProcessingTask(int jobID, int taskID, int replicaCount, Task task) {
        this.jobID = jobID;
        this.taskID = taskID;
        this.replicaCount = replicaCount;
        this.task = task;

        this.resultList = new ArrayList<byte[]>();
        this.taskStatusMap = new HashMap<Integer, TaskStatus>();     // maps processing node of task, to task status
        validatedResult = new byte[1];

    }

    public int getResultReceivedCount() {
        return resultReceivedCount;
    }

    public void incrementResultReceivedCount() {
        this.resultReceivedCount++;
    }

    public int getJobID() {
        return jobID;
    }

    public int getTaskID() {
        return taskID;
    }

    public Task getTask() {
        return task;
    }

    public boolean addResult(byte[] result) {
        return this.resultList.add(result);
    }

    public ArrayList<byte[]> getResultList() {
        return this.resultList;
    }

    public Map<Integer, TaskStatus> getTaskStatusMap() {
        return taskStatusMap;
    }

    public void addToStatusMap(Integer processingNodeID, TaskStatus status) {
        taskStatusMap.put(processingNodeID, status);
    }

    public byte[] getValidatedResult() {
        return validatedResult;
    }

    public void setValidatedResult(byte[] validatedResult) {
        this.validatedResult = validatedResult;
    }

    public long getTimeLastUpdated() {
        return timeLastUpdated;
    }

    public void setTimeLastUpdated(long timeLastUpdated) {
        this.timeLastUpdated = timeLastUpdated;
    }

    public NodeInfo getBackupNode() {
        return backupNode;
    }

    public void setBackupNode(NodeInfo backupNode) {
        this.backupNode = backupNode;
    }

    public int getReplicaCount() {
        return replicaCount;
    }

    public void setReplicaCount(int replicaCount) {
        this.replicaCount = replicaCount;
    }

    //    public byte[] getResult() {
//        return result;
//    }
//
//    public void setResult(byte[] result) {
//        this.result = result;
//    }

//    public NodeInfo getProcessingNode() {
//        return processingNode;
//    }

//    public void setProcessingNode(NodeInfo processingNode) {
//        this.processingNode = processingNode;
//    }

//    public TaskStatus getStatus() {
//        return status;
//    }
//
//    public void addToStatusMap(TaskStatus status) {
//        this.status = status;
//    }

//    public ReplicationStatus getReplicationStatus(){
//        return replicationStatus;
//    }
//
//    public void setReplicationStatus(ReplicationStatus replicationStatus){
//        this.replicationStatus = replicationStatus;
//    }

//    public ConcurrentHashMap<Integer, ReplicaTask> getReplicaTaskMap() {
//        return replicaTaskMap;
//    }
//
//    public void setReplicaTaskMap(ConcurrentHashMap<Integer, ReplicaTask> replicaTaskMap) {
//        this.replicaTaskMap = replicaTaskMap;
//    }

//    public TaskStatus getStatusForAllReplicas() {
//        return statusForAllReplicas;
//    }
//
//    public void setStatusForAllReplicas(TaskStatus statusForAllReplicas) {
//        this.statusForAllReplicas = statusForAllReplicas;
//    }

}
