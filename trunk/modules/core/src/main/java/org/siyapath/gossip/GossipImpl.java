package org.siyapath.gossip;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.siyapath.*;
import org.siyapath.service.*;

import org.siyapath.utils.CommonUtils;

import javax.xml.bind.SchemaOutputResolver;
import java.net.ConnectException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class GossipImpl {

    private static final Log log = LogFactory.getLog(GossipImpl.class);
    private NodeContext nodeContext;


    public GossipImpl(NodeContext nodeContext) {
        this.nodeContext = nodeContext;
    }

    /**
     * Is invoked when a remote node selects this node and gossips the member set.
     * Updates this node's member set by merging the two sets.
     *
     * @param receivedMemberSet member set received from remote node
     * @return the initial member set of this node
     */
    public Set<NodeInfo> memberDiscovery(NodeInfo nodeInfo, Set<NodeInfo> receivedMemberSet) {
        log.info("Remote node invoked member gossip with this node");
        Set<NodeInfo> initialSet = nodeContext.getMemberSet();
        Set<NodeInfo> newSet = mergeSets(initialSet, receivedMemberSet);
        nodeContext.updateMemberSet(newSet);
        nodeContext.addMemNodeSet(nodeInfo, receivedMemberSet);

        return initialSet;
    }

    /**
     * Is invoked when a remote node requests for
     * a member set from bootstrapper
     *
     * @return the known member set
     */
    public Set<NodeInfo> getMembers() {
        Set<NodeInfo> members = nodeContext.getMemberSet();
        Set<NodeInfo> newSet = new HashSet<NodeInfo>();
        if (members.size() < SiyapathConstants.BOOSTRAPPER_MEMBER_SET_LIMIT) {
            return members;
        } else {
            Iterator memIter = members.iterator();
            while (memIter.hasNext() && newSet.size() < SiyapathConstants.BOOSTRAPPER_MEMBER_SET_LIMIT) {
                NodeInfo member = (NodeInfo) memIter.next();
                newSet.add(member);
            }
            return newSet;
        }
    }

    /**
     * Is invoked when a remote node selects this node and gossips its resource.
     * Updates this node's node resource set
     *
     * @param receivedNodeResource received from remote node
     * @return the node resource of this node
     */
    public NodeResource resourceGossip(NodeResource receivedNodeResource) {
        log.info("Remote node invoked member gossip resource with this node :" + nodeContext.getNodeInfo().getPort());
        HashSet<NodeResource> initialSet = nodeContext.getMemberResourceSet();
        HashSet<NodeResource> newSet = mergeNewNodeResource(initialSet, receivedNodeResource);
        nodeContext.updateMemberResourceSet(newSet);
        return nodeContext.getNodeResource();
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
                HashSet<NodeInfo> nodesToGossip = getDiff(randomMember);
                Set<NodeInfo> discoveredNodes = CommonUtils.deSerialize(client.memberDiscovery(CommonUtils.serialize(nodeContext.getNodeInfo()), CommonUtils.serialize(nodesToGossip)));
                Set<NodeInfo> newSet = mergeSets(nodeContext.getMemberSet(), discoveredNodes);
                nodeContext.updateMemberSet(newSet);
                nodeContext.addMemNodeSet(randomMember, discoveredNodes);
                log.info("Members fetched " + discoveredNodes.size());
//                for (NodeInfo i : discoveredNodes) {
//                    log.info(i);
//                }

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
     * Initiates gossiping of node resource. Select a random member form the known member
     * list and gossip current statistics of node resource list by calling resourcesGossip service &
     * update the current list of node with resource with the response
     */
    public void resourceGossiper() {

        NodeInfo randomMember = nodeContext.getRandomMember();
        log.info("Getting a random member to gossip resources:" + randomMember);
        if (randomMember != null) {
            TTransport transport = new TSocket("localhost", randomMember.getPort());
            try {
                transport.open();
                TProtocol protocol = new TBinaryProtocol(transport);
                Siyapath.Client client = new Siyapath.Client(protocol);
                NodeResource discoveredNodeResource = CommonUtils.deSerialize(client.resourceGossip(CommonUtils.serialize(nodeContext.getNodeResource())));
                HashSet<NodeResource> newSet = mergeNewNodeResource(nodeContext.getMemberResourceSet(), discoveredNodeResource);
                nodeContext.updateMemberResourceSet(newSet);
                log.info("Node Resource Fetched:" + discoveredNodeResource.getNodeInfo().getPort());

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
     *
     * @param initialSet
     * @param discoveredSet
     * @return new merged Set of node info
     */
    private Set<NodeInfo> mergeSets(Set initialSet, Set discoveredSet) {
        Set<NodeInfo> newSet = initialSet;
        Set<NodeInfo> tempSet = new HashSet<NodeInfo>();
        for (Iterator iterator = discoveredSet.iterator(); tempSet.size() < 0.5 * SiyapathConstants.MEMBER_SET_LIMIT && iterator.hasNext(); ) {
            NodeInfo member = (NodeInfo) iterator.next();
            if (!initialSet.contains(member) && !member.equals(nodeContext.getNodeInfo())) {
                tempSet.add(member);
            }
        }
        while (newSet.size() > 0.5 * SiyapathConstants.MEMBER_SET_LIMIT) {
            newSet.remove(newSet.iterator().next());
        }
        newSet.addAll(tempSet);
        return newSet;
    }

    /**
     * @param initialSet
     * @param discoveredNodeResource
     * @return new merged Set of node resource
     */
    private HashSet<NodeResource> mergeNewNodeResource(HashSet initialSet, NodeResource discoveredNodeResource) {
        HashSet<NodeResource> newSet = initialSet;
        if (newSet.size() < SiyapathConstants.RESOURCE_MEMBER_SET_LIMIT) {
            if (!discoveredNodeResource.equals(nodeContext.getNodeResource())) {
                newSet.add(discoveredNodeResource);
            }
        } else {
            if (!discoveredNodeResource.equals(nodeContext.getNodeResource())) {
                newSet.remove(newSet.iterator().next());
                newSet.add(discoveredNodeResource);
            }

        }

        return newSet;
    }

    private HashSet<NodeInfo> getDiff(NodeInfo gossipMember) {
        HashSet<NodeInfo> tempSet = new HashSet<NodeInfo>();
        HashSet<NodeInfo> initialSet = (HashSet<NodeInfo>) nodeContext.getMemberSet();
        HashSet<NodeInfo> memberSet = (HashSet<NodeInfo>) nodeContext.getMemNodeSet(gossipMember);
        if (memberSet != null) {
            Iterator members = memberSet.iterator();
            while (members.hasNext()) {
                NodeInfo newNode = (NodeInfo) members.next();
                if (!initialSet.contains(newNode)) {
                    tempSet.add(newNode);
                }
            }
            return tempSet;
        }
        return initialSet;
    }
}
