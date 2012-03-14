package org.siyapath.gossip;

import org.siyapath.NodeInfo;
import org.siyapath.NodeContext;

import java.util.HashSet;
import java.util.Set;


public class GossipImpl {
    private NodeContext nodeContext = NodeContext.getInstance();

    public GossipImpl() {
    }

    public Set<NodeInfo> getMembers() {
        return nodeContext.getMemberSet();
    }

    public Set<NodeInfo> memberDiscovery(Set<NodeInfo> receivedMemberSet) {
        Set<NodeInfo> initialSet = nodeContext.getMemberSet();
        Set<NodeInfo> newSet = nodeContext.getMemberSet();
        Set<NodeInfo> tempSet = new HashSet<NodeInfo>();
        for (int i = 0; i < initialSet.size() * 0.5; i++) {
            NodeInfo member = receivedMemberSet.iterator().next();
            tempSet.add(member);
        }
        for (NodeInfo i : tempSet) {
            newSet.remove(newSet.iterator().next());
        }
        newSet.addAll(tempSet);
        nodeContext.updateMemberSet(newSet);
        return initialSet;
    }
}
