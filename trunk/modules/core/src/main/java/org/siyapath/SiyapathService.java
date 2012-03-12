package org.siyapath;

import org.apache.thrift.TException;
import org.siyapath.gossip.GossipImpl;
import org.siyapath.job.TaskProcessor;

import java.util.Map;
import java.util.Set;

public class SiyapathService implements Siyapath.Iface {

    private NodeContext nodeContext;
    private GossipImpl gossipImpl;

    public SiyapathService() {
        this.nodeContext = NodeContext.getInstance();
        this.gossipImpl = new GossipImpl();
    }

    @Override
    public GossipData gossip(GossipData gData) throws TException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean notifyPresence(int nodeID) throws TException {
        nodeContext.addMember((nodeID));
        return true;
    }

    @Override
    public Set<Integer> memberDiscovery(Set<Integer> knownNodes) throws TException {
        return gossipImpl.memberDiscovery(knownNodes);
    }

    @Override
    public Set<Integer> getMembers() throws TException {
        return gossipImpl.getMembers();
    }

    @Override
    public boolean isAlive() throws TException {
        return true;
    }

    @Override
    public boolean requestBecomeBackup(int nodeID, int taskID) throws TException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String submitJob(Map<Integer, Task> tasks, int jobID) throws TException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean submitTask(Task task) throws TException {
        TaskProcessor taskProcessor = new TaskProcessor(task, task.getClassName());
        taskProcessor.processTask();
        return true;
    }

    @Override
    public String getJobStatus(int jobID) throws TException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<Integer, Task> getJobResult(int jobID) throws TException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String userLogin(String username, String password) throws TException {
        if (username.equals("admin") && password.equals("admin")) {
            return "sucess";
        } else {
            return "failure";
        }
    }


}
