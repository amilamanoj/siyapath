package org.siyapath.gossip;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.siyapath.NodeInfo;
import org.siyapath.NodeContext;
import org.siyapath.Siyapath;
import org.siyapath.utils.CommonUtils;

import java.net.ConnectException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class GossipImpl {

    private static final Log log = LogFactory.getLog(GossipImpl.class);
    private NodeContext nodeContext = NodeContext.getInstance();
    private static final int MEMBER_SET_LIMIT = 10;

    public GossipImpl() {
    }

    /**
     * Is invoked when a remote node selects this node and gossips the member set.
     * Updates this node's member set by merging the two sets.
     *
     * @param receivedMemberSet member set received from remote node
     * @return the initial member set of this node
     */
    public Set<NodeInfo> memberDiscovery(Set<NodeInfo> receivedMemberSet) {
        log.info("Remote node invoked member gossip with this node");
        Set<NodeInfo> initialSet = nodeContext.getMemberSet();
        Set<NodeInfo> newSet = mergeSets(initialSet, receivedMemberSet);
        nodeContext.updateMemberSet(newSet);
        return initialSet;
    }

    /**
     * Initiates gossiping of member set. Select a random member form the known member
     * list and gossip current member list by calling memberDiscovery service &
     * update the current list with the response
     */
    public void memberGossiper() {

        NodeInfo randomMember = nodeContext.getRandomMember();
        log.info("Getting a random member to gossip:" + randomMember);
        if (randomMember != null) {
            TTransport transport = new TSocket("localhost", randomMember.getPort());
            try {
                transport.open();
                TProtocol protocol = new TBinaryProtocol(transport);
                Siyapath.Client client = new Siyapath.Client(protocol);
                Set<NodeInfo> discoveredNodes = CommonUtils.deSerialize(client.memberDiscovery(CommonUtils.serialize(nodeContext.getMemberSet())));
                Set<NodeInfo> newSet = mergeSets(nodeContext.getMemberSet(), discoveredNodes);
                nodeContext.updateMemberSet(newSet);
                log.info("members Fetched:");
                for (NodeInfo i : discoveredNodes) {
                    log.info(i);
                }

            } catch (TTransportException e) {
                if (e.getCause() instanceof ConnectException) {
                    System.out.println("Could not connect to the bootstrapper :(");
                }

            } catch (TException e) {
                e.printStackTrace();

            } finally {
                transport.close();
            }
        }
    }

    /**
     * Merges two Sets keeping the resulting set size under a defined limit
     * @param initialSet
     * @param discoveredSet
     * @return new merged Set of node info
     */
    private Set<NodeInfo> mergeSets(Set initialSet, Set discoveredSet){
        Set<NodeInfo> newSet = initialSet;
        Set<NodeInfo> tempSet = new HashSet<NodeInfo>();
        for (Iterator iterator = discoveredSet.iterator(); tempSet.size() < 0.5 * MEMBER_SET_LIMIT && iterator.hasNext(); ) {
            NodeInfo member = (NodeInfo) iterator.next();
            if (!initialSet.contains(member) && !member.equals(nodeContext.getNodeInfo())) {
                tempSet.add(member);
            }
        }
        while (newSet.size() > 0.5 * MEMBER_SET_LIMIT) {
            newSet.remove(newSet.iterator().next());
        }
        newSet.addAll(tempSet);
        return newSet;
    }
}
