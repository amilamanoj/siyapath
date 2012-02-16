package org.siyapath.gossip;

import org.siyapath.GossipData;
import org.siyapath.NodeContext;
import org.siyapath.NodeData;
import org.siyapath.SiyapathService;

import java.util.Set;


public class GossipImpl {
    private NodeContext nodeContext = NodeContext.getInstance();

    public GossipImpl() {
    }

    public Set<Integer> getMembers() {
        return nodeContext.getMemberSet();
    }

}
