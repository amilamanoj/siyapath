package org.siyapath;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.thrift.TException;
import org.siyapath.gossip.GossipImpl;
import org.siyapath.job.JobHandler;
import org.siyapath.job.TaskProcessor;
import org.siyapath.utils.CommonUtils;
import org.siyapath.service.*;

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
    public Set<NodeData> memberDiscovery(NodeData nodeData,Set<NodeData> knownNodes) throws TException {
        return CommonUtils.serialize(gossipImpl.memberDiscovery(CommonUtils.deSerialize(nodeData),CommonUtils.deSerialize(knownNodes)));
    }

    /**
     * @return
     * @throws TException
     */
    @Override
    public Set<NodeData> getMembers() throws TException {
        return CommonUtils.serialize(nodeContext.getMemberSet());
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
     * @param nodeID
     * @param taskID
     * @return true if node becomes backup node for processing job, false otherwise
     * @throws TException
     */
    @Override
    public boolean requestBecomeBackup(int nodeID, int taskID) throws TException {
        throw new UnsupportedOperationException();
    }

    /**
     * @param tasks
     * @param jobID
     * @return String success if job successfully submitted
     * @throws TException
     */
    @Override
    public String submitJob(int jobID, NodeData sender, Map<Integer, Task> tasks) throws TException {
        log.info("Received a new job. JobID:" + jobID + " from: "+ CommonUtils.deSerialize(sender));
        JobHandler jobHandler = new JobHandler(nodeContext, jobID, tasks);
        nodeContext.addJob(jobHandler);
        jobHandler.startScheduling();
        return "JobHandlerNode:" + nodeContext.getNodeInfo().getNodeId() + " JobID:" + jobID + ":" +  "Accepted";
    }

    /**
     * @param task
     * @return true if task successfully submitted, false otherwise
     * @throws TException
     */
    @Override
    public boolean submitTask(Task task) throws TException {
        log.info("Received a new task. TaskID:" + task.getTaskID() + " from: "+ CommonUtils.deSerialize(task.getSender()));
        TaskProcessor taskProcessor = new TaskProcessor(task, nodeContext);
        taskProcessor.processTask();
        return true;
    }

    /**
     * @param jobID
     * @return
     * @throws TException
     */
    @Override
    public boolean getJobStatusFromJobHandler (int jobID) throws TException {
        throw new UnsupportedOperationException();





    }

    @Override
    public boolean getTaskStatusFromTaskProcessor (int taskID) throws TException {
        throw new UnsupportedOperationException();





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
//        JobHandler taskDistributor = new JobHandler(task, nodeContext);
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
