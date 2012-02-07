package org.siyapath;

import org.apache.thrift.TException;

import java.util.Set;

public class GossipServiceHandler implements GossipService.Iface {

    private NodeContext nodeContext;

    public GossipServiceHandler(NodeContext nodeContext) {
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


}
