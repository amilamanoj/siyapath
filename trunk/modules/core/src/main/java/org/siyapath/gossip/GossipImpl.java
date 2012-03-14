package org.siyapath.gossip;

import org.siyapath.GossipData;
import org.siyapath.NodeContext;
import org.siyapath.NodeData;
import org.siyapath.SiyapathService;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class GossipImpl {
    private NodeContext nodeContext = NodeContext.getInstance();

    public GossipImpl() {
    }

    public Set<Integer> memberDiscovery(Set<Integer> receivedMemberSet) {
        Set<Integer> initialSet = nodeContext.getMemberSet();
        Set<Integer> newSet = nodeContext.getMemberSet();
        Set<Integer> tempSet = new HashSet<Integer>();
        for (Iterator iterator = receivedMemberSet.iterator(); tempSet.size() < 0.5 * initialSet.size() && iterator.hasNext(); ) {
            Integer member = (Integer) iterator.next();
            if (!initialSet.contains(member) && member != nodeContext.getNodeID()) {
                tempSet.add(member);
            }
        }
        for (Integer i : tempSet) {
            newSet.remove(newSet.iterator().next());
        }
        newSet.addAll(tempSet);
        nodeContext.updateMemberSet(newSet);
        return initialSet;
    }
}
