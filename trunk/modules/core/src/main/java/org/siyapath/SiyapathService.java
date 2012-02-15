package org.siyapath;

import org.apache.thrift.TException;

import java.util.Map;
import java.util.Set;

public class SiyapathService implements GossipService.Iface {

    private NodeContext nodeContext;

    public SiyapathService(NodeContext nodeContext) {
        this.nodeContext = nodeContext;
    }

    @Override
    public GossipData gossip(GossipData gData) throws TException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean notifyPresence(int nodeID) throws TException {
        nodeContext.addMember(nodeID);
        return true;
    }

    @Override
    public Set<String> memberDiscovery(Set<String> knownNodes) throws TException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Integer> getMembers() throws TException {
        return nodeContext.getMemberSet();
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
    public String getJobStatus(int jobID) throws org.apache.thrift.TException{
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<Integer, org.siyapath.Task> getJobResult(int jobID) throws TException {
        throw new UnsupportedOperationException();
    }


}
