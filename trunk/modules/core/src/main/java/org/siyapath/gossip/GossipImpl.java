package org.siyapath.gossip;

import org.siyapath.NodeInfo;
import org.siyapath.NodeContext;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class GossipImpl {
    private NodeContext nodeContext = NodeContext.getInstance();

    public GossipImpl() {
    }

    public Set<NodeInfo> memberDiscovery(Set<NodeInfo> receivedMemberSet) {
        Set<NodeInfo> initialSet = nodeContext.getMemberSet();
        Set<NodeInfo> newSet = nodeContext.getMemberSet();
        Set<NodeInfo> tempSet = new HashSet<NodeInfo>();
        for (Iterator iterator = receivedMemberSet.iterator(); tempSet.size() < 0.5 * initialSet.size() && iterator.hasNext(); ) {
            NodeInfo member = (NodeInfo) iterator.next();
            if (!initialSet.contains(member) && !member.equals(nodeContext.getNodeInfo())) {
                tempSet.add(member);
            }
        }
        for (int i = 0; i < tempSet.size(); i++) {
            newSet.remove(newSet.iterator().next());
        }
        newSet.addAll(tempSet);
        nodeContext.updateMemberSet(newSet);
        return initialSet;
    }
}
