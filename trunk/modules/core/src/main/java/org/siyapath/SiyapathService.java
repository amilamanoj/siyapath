package org.siyapath;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.thrift.TException;
import org.siyapath.gossip.GossipImpl;
import org.siyapath.job.TaskDistributor;
import org.siyapath.job.TaskProcessor;
import org.siyapath.utils.CommonUtils;

import java.util.Map;
import java.util.Set;

public class SiyapathService implements Siyapath.Iface {
    private static final Log log = LogFactory.getLog(PeerListener.class);

    private NodeContext nodeContext;
    private GossipImpl gossipImpl;

    public SiyapathService() {
        this.nodeContext = NodeContext.getInstance();
        this.gossipImpl = new GossipImpl();
    }

    /**
     *
     * @param gData
     * @return
     * @throws TException
     */
    @Override
    public GossipData gossip(GossipData gData) throws TException {
        throw new UnsupportedOperationException();
    }

    /**
     *
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
     *
     * @param knownNodes
     * @return
     * @throws TException
     */
    @Override
    public Set<NodeData> memberDiscovery(Set<NodeData> knownNodes) throws TException {
        return CommonUtils.serialize(gossipImpl.memberDiscovery(CommonUtils.deSerialize(knownNodes)));
    }

    /**
     *
     * @return
     * @throws TException
     */
    @Override
    public Set<NodeData> getMembers() throws TException {
        return CommonUtils.serialize(nodeContext.getMemberSet());
    }

    /**
     *
     * @return true if node is alive, false otherwise
     * @throws TException
     */
    @Override
    public boolean isAlive() throws TException {
        return true;
    }

    /**
     *
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
     *
     * @param tasks
     * @param jobID
     * @return String success if job successfully submitted
     * @throws TException
     */
    @Override
    public String submitJob(int jobID, NodeData sender, Map<Integer, Task> tasks ) throws TException {
        log.info("Received the job: " + jobID);
        Task firstTask = tasks.get(new Integer(1));
        TaskDistributor taskDistributor = new TaskDistributor(firstTask);
        taskDistributor.sendTaskToProcessingNode();
//        Task firstTask = tasks.get(new Integer(1));
//        TaskProcessor taskProcessor = new TaskProcessor(firstTask, firstTask.getClassName());
//        taskProcessor.processTask();
        return "sucess";
    }

    /**
     *
     * @param task
     * @return true if task successfully submitted, false otherwise
     * @throws TException
     */
    @Override
    public boolean submitTask(Task task) throws TException {
        TaskProcessor taskProcessor = new TaskProcessor(task);
        taskProcessor.processTask();
        return true;
    }

    /**
     *
     * @param jobID
     * @return
     * @throws TException
     */
    @Override
    public String getJobStatus(int jobID) throws TException {
        throw new UnsupportedOperationException();
    }

    /**
     *
     * @param jobID
     * @return
     * @throws TException
     */
    @Override
    public Map<Integer, Task> getJobResult(int jobID) throws TException {
        throw new UnsupportedOperationException();
    }

    /**
     *
     * @param task
     * @return true if computed result sent successfully, false otherwise
     */
    @Override
    public boolean sendTaskResult(Task task){
        TaskDistributor taskDistributor = new TaskDistributor(task);
        taskDistributor.sendResultToUserNode();
        return true;
    }

    /**
     *
     * @param username
     * @param password
     * @return String success if user can be authenticated, failure otherwise
     * @throws TException
     */
    @Override
    public String userLogin(String username, String password) throws TException {
        if (username.equals("admin") && password.equals("admin")) {
            return "sucess";
        } else {
            return "failure";
        }
    }


}
