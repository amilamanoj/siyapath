package org.siyapath;

import org.apache.thrift.TException;
import org.siyapath.gossip.GossipImpl;

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
    public Set<String> memberDiscovery(Set<String> knownNodes) throws TException {
        throw new UnsupportedOperationException();
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
        throw new UnsupportedOperationException();
//        processTask(task);
    }

    @Override
    public String getJobStatus(int jobID) throws TException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<Integer, Task> getJobResult(int jobID) throws TException {
        throw new UnsupportedOperationException();
    }


}
