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
    public void isAlive() throws TException {

    }

    @Override
    public boolean backupRequest() throws TException {
        return false;
    }
}
