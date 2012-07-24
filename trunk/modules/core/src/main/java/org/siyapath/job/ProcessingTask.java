package org.siyapath.job;

import org.siyapath.NodeInfo;

public class ProcessingTask {

    private int jobID;
    private int taskID;
    private NodeInfo processingNode;
    private TaskStatus status;
    private String result;

    public enum TaskStatus {
        RECEIVED, PROCESSING, DONE
    }

    public ProcessingTask(int jobID, int taskID, TaskStatus status) {
        this.jobID = jobID;
        this.taskID = taskID;
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
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
}
