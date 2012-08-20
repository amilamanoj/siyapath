package org.siyapath.job;

import org.siyapath.NodeInfo;
import org.siyapath.service.TaskStatus;

import java.nio.ByteBuffer;

public class ProcessingTask {

    private int jobID;
    private int taskID;
    private NodeInfo processingNode;
    private TaskStatus status;
    private byte[] result;
    private ReplicationStatus replicationStatus;
    private long timeLastUpdated;
    private NodeInfo backupNode;


    public enum ReplicationStatus {
        ORIGINAL, REPLICA
    }

    public ProcessingTask(int jobID, int taskID, TaskStatus status) {
        this.jobID = jobID;
        this.taskID = taskID;
        this.status = status;
        this.result = new byte[1];
    }

    public int getJobID() {
        return jobID;
    }

    public int getTaskID() {
        return taskID;
    }

    public byte[] getResult() {
        return result;
    }

    public void setResult(byte[] result) {
        this.result = result;
    }

    public NodeInfo getProcessingNode() {
        return processingNode;
    }

    public void setProcessingNode(NodeInfo processingNode) {
        this.processingNode = processingNode;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public ReplicationStatus getReplicationStatus(){
        return replicationStatus;
    }

    public void setReplicationStatus(ReplicationStatus replicationStatus){
        this.replicationStatus = replicationStatus;
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
}
