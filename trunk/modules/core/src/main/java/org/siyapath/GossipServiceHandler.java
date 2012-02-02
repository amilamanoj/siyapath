package org.siyapath;

import org.apache.thrift.TException;

import java.util.Set;

public class GossipServiceHandler  implements GossipService.Iface{

    @Override
    public GossipData gossip(GossipData gData) throws TException {
        return null;
    }

    @Override
    public Set<String> memberDiscovery(Set<String> knownNodes) throws TException {
        return null;
    }

    @Override
    public Set<String> getMembers() throws TException {
        return null;
    }

    @Override
    public boolean isAlive(int nodeID) throws TException {
        return false;
    }

    @Override
    public boolean requestBecomeBackup(int nodeID, int taskID) throws TException {
        return false;
    }
}
