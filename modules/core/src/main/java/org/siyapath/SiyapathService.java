package org.siyapath;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.thrift.TException;
import org.siyapath.common.BackupHandler;
import org.siyapath.gossip.GossipImpl;
import org.siyapath.job.TaskProcessor;
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
     * @param resourceData
     * @return
     * @throws TException
     */
    @Override
    public NodeResourceData resourceGossip(NodeResourceData resourceData) throws TException {
        return CommonUtils.serialize(gossipImpl.resourceGossip(CommonUtils.deSerialize(resourceData)));
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
     * @param jobID
     * @param node
     * @return true if node becomes backup node for processing job, false otherwise
     * @throws TException
     */
    @Override
    public boolean requestBecomeBackup(int jobID, NodeData node) throws TException {
        log.info("Received backup request. JobID:" + jobID + " from:" + CommonUtils.deSerialize
                (node));
        BackupHandler backupHandler = new BackupHandler(nodeContext);
        return backupHandler.requestBecomeBackup(jobID, CommonUtils.deSerialize(node));
    }

    /**
     * @param job received job
     * @return String success if job is accepted
     * @throws TException
     */
    @Override
    public String submitJob(Job job) throws TException {
        log.info("Received a new job. JobID:" + job.getJobID() + " from: " + CommonUtils.deSerialize(job.getUser()));
        nodeContext.getJobProcessor().addNewJob(job);
        return "JobHandlerNode:" + nodeContext.getNodeInfo().getNodeId() + " JobID:" + job.getJobID() + ":" + "Accepted";
    }

    /**
     * @param task
     * @return true if task successfully submitted, false otherwise
     * @throws TException
     */
    @Override
    public boolean submitTask(Task task) throws TException {
        log.info("Received a new task. TaskID:" + task.getTaskID() + " from: " + CommonUtils.deSerialize(task.getSender()));
        TaskProcessor taskProcessor = new TaskProcessor(task, nodeContext);
        taskProcessor.startProcessing();
        return true;
    }

    /**
     * @param jobID
     * @return
     * @throws TException
     */
    @Override
    public boolean getJobStatusFromJobHandler(int jobID, int port) throws TException {
        //send the ip, port n stuff as params, codegen idl and replace
        boolean jobStatus;
        NodeInfo handlerNodeInfo = new NodeInfo();
//        handlerNodeInfo.setIp();
        handlerNodeInfo.setPort(port);
//        handlerNodeInfo.setNodeId();
        NodeContext handlerNodeContext = new NodeContext(handlerNodeInfo);
//        JobProcessor jobHandler = new JobProcessor(handlerNodeContext,jobID, new HashMap<Integer,Task>());
//        jobStatus = jobHandler.thriftCall(jobID);
//        return jobStatus;
        return false;

    }

    @Override
    public boolean getTaskStatusFromTaskProcessor(Task task, int port) throws TException {
        boolean taskStatus;
        NodeInfo taskProcessorNodeInfo = new NodeInfo();
        taskProcessorNodeInfo.setPort(port);
        NodeContext taskProcessorNodeContext = new NodeContext(taskProcessorNodeInfo);
        TaskProcessor taskProcessor = new TaskProcessor(task, taskProcessorNodeContext);
        taskStatus = taskProcessor.isTaskStatus();
        return taskStatus;
    }

    /**
     * @param jobID
     * @return
     * @throws TException
     */
    @Override
    public Map<Integer, Task> getJobResult(int jobID) throws TException {
        throw new UnsupportedOperationException();
    }

    /**
     * @param task
     * @return true if computed result sent successfully, false otherwise
     */
    @Override
    public boolean sendTaskResult(Task task) {
//        JobProcessor taskDistributor = new JobProcessor(task, nodeContext);
//        taskDistributor.sendResultToUserNode();
        return true;
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


}
