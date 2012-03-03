package org.siyapath.gossip;

import org.siyapath.GossipData;
import org.siyapath.NodeContext;
import org.siyapath.NodeData;
import org.siyapath.SiyapathService;

import java.util.HashSet;
import java.util.Set;


public class GossipImpl {
    private NodeContext nodeContext = NodeContext.getInstance();

    public GossipImpl() {
    }

    public Set<Integer> getMembers() {
        return nodeContext.getMemberSet();
    }

    public Set<Integer> memberDiscovery(Set<Integer> receivedMemberSet) {
        Set<Integer> initialSet = nodeContext.getMemberSet();
        Set<Integer> newSet = nodeContext.getMemberSet();
        Set<Integer> tempSet = new HashSet<Integer>();
        for (int i = 0; i < initialSet.size() * 0.5; i++) {
            Integer member = receivedMemberSet.iterator().next();
            tempSet.add(member);
        }
        for (Integer i : tempSet) {
            newSet.remove(newSet.iterator().next());
        }
        newSet.addAll(tempSet);
        nodeContext.updateMemberSet(newSet);
        return initialSet;
    }
}
