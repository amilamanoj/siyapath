package org.siyapath;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.thrift.TException;
import org.siyapath.gossip.GossipImpl;
import org.siyapath.task.TaskProcessor;
import org.siyapath.service.*;
import org.siyapath.utils.CommonUtils;

import java.util.Map;
import java.util.Set;

public class SiyapathService implements Siyapath.Iface {
    private static final Log log = LogFactory.getLog(PeerListener.class);

    private NodeContext nodeContext;
    private GossipImpl gossipImpl;

    public SiyapathService(NodeContext nodeContext) {
        this.nodeContext = nodeContext;
        this.gossipImpl = new GossipImpl(nodeContext);
    }

    /**
     * @param knownResourceNodes
     * @return
     * @throws TException
     */
    @Override
    public Map<Integer, NodeResourceData> resourceGossip(Map<Integer, NodeResourceData> knownResourceNodes) throws TException {
        return CommonUtils.serialize(gossipImpl.resourceGossip(CommonUtils.deSerialize(knownResourceNodes)));
    }

    /**
     * @param nodeData
     * @return
     * @throws TException
     */
    @Override
    public boolean notifyPresence(NodeData nodeData) throws TException {
        nodeContext.addMember((CommonUtils.deSerialize(nodeData)));
        return true;
    }

    /**
     * @param knownNodes
     * @return
     * @throws TException
     */
    @Override
    public Set<NodeData> memberDiscovery(NodeData nodeData, Set<NodeData> knownNodes) throws TException {
        return CommonUtils.serialize(gossipImpl.memberDiscovery(CommonUtils.deSerialize(nodeData), CommonUtils.deSerialize(knownNodes)));
    }

    /**
     * @return
     * @throws TException
     */
    @Override
    public Set<NodeData> getMembers() throws TException {
        return CommonUtils.serialize(gossipImpl.getMembers());
    }

    /**
     * @return true if node is alive, false otherwise
     * @throws TException
     */
    @Override
    public boolean isAlive() throws TException {
        return true;
    }

    /**
     * @param job
     * @param node
     * @return true if node becomes backup node for processing job, false otherwise
     * @throws TException
     */
    @Override
    public boolean requestBecomeBackup(Job job, NodeData node) throws TException {
        log.info("Received backup request. JobID:" + job.getJobID() + " from:" + CommonUtils.deSerialize
                (node));
        return nodeContext.getBackupHandler().requestBecomeBackup(job, CommonUtils.deSerialize(node));
    }

    @Override
    public void endBackup() throws TException {
        log.info("Job is complete. Ending backup.");
        nodeContext.getBackupHandler().endBackup();
    }

    /**
     * @param job received job
     * @return String success if job is accepted
     * @throws TException
     */
    @Override
    public boolean submitJob(Job job) throws TException {
        log.info("Received a new job. JobID:" + job.getJobID() + " from: " + CommonUtils.deSerialize(job.getUser()));
       if(nodeContext.getNodeResource().isIdle()||nodeContext.getNodeResource().getNodeStatus()==NodeStatus.DISTRIBUTING){
        nodeContext.getJobProcessor().addNewJob(job);
           return true;
       }else {
           log.info("Rejecting Job-  ID:" + job.getJobID() + " from: " + CommonUtils.deSerialize(job.getUser()));
           return false;
       }
    }

    /**
     * @param task
     * @return true if task successfully submitted, false otherwise
     * @throws TException
     */
    @Override
    public synchronized boolean submitTask(Task task) throws TException {
        log.info("Received a new task. TaskID:" + task.getTaskID() + " from: " + CommonUtils.deSerialize(task.getSender()));
        if (nodeContext.getNodeResource().isIdle()||nodeContext.getNodeResource().getNodeStatus()==NodeStatus.PROCESSING_IDLE){
        TaskProcessor taskProcessor = new TaskProcessor("TaskProcessor ID:" + task.getTaskID(), task, nodeContext);
        taskProcessor.start();
            return true;
        }else {
             log.info("Rejecting Task-  ID:" + task.getTaskID() + " from: " + CommonUtils.deSerialize(task.getSender()));
            return false;
        }
    }

    /**
     *
     * @param jobId
     * @return task status map for the given JobId, with the mapping of taskID to task completion status
     */
    @Override
    public Map<Integer,TaskResult> getJobStatus(int jobId){
        return nodeContext.getJobProcessor().getTaskStatusesForJob(jobId);

    }

    /**
     * @param jobID
     * @return
     * @throws TException
     */
    @Override
    public Map<Integer, TaskResult> getJobResult(int jobID) throws TException {
        return null;
    }

    /**
     * @param result
     * @return true if computed result sent successfully, false otherwise
     */
    @Override
    public boolean sendTaskResult(Result result) {
        log.info("Received results: JobID:" + result.getJobID() + " TaskID: " + result.getTaskID());
        nodeContext.getJobProcessor().resultsReceived(result);
        return true;
    }

    @Override
    public boolean sendTaskResultToBackup(Result result) throws TException {
        log.info("Received results to backup: JobID:" + result.getJobID() + " TaskID: " + result
                .getTaskID());
        nodeContext.getBackupHandler().updateTaskResult(result);
        return true;
    }

    @Override
    public void notifyTaskLiveness(int taskID) throws TException {
        log.debug("Received update from task processor: TaskID: " + taskID);
        nodeContext.getJobProcessor().taskUpdateReceived(taskID);
    }

    /**
     * @param username
     * @param password
     * @return String success if user can be authenticated, failure otherwise
     * @throws TException
     */
    @Override
    public String userLogin(String username, String password) throws TException {
        if (username.equals("admin") && password.equals("admin")) {
            return "success";
        } else {
            return "failure";
        }
    }

    /**
     *
     * @return Current Node Status
     * @throws TException
     */
    @Override
    public NodeStatus getNodeStatus() throws TException {
        return nodeContext.getNodeResource().getNodeStatus();
    }


}
