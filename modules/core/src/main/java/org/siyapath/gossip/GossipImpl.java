package org.siyapath.gossip;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.siyapath.NodeContext;
import org.siyapath.NodeInfo;
import org.siyapath.NodeResource;
import org.siyapath.SiyapathConstants;
import org.siyapath.service.Siyapath;
import org.siyapath.utils.CommonUtils;

import java.net.ConnectException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.Set;


/**
 * This class provides overall gossip related operation implementation
 */
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
        Set<NodeInfo> initialSet = (Set<NodeInfo>) ((HashSet<NodeInfo>) nodeContext.getMemberSet()).clone();
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
        if (members.size() <= SiyapathConstants.MEMBER_SET_LIMIT) {
            return members;
        } else {
            Iterator memIter = members.iterator();
            while (memIter.hasNext() && newSet.size() < SiyapathConstants.MEMBER_SET_LIMIT) {
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
     * @param receivedResourceNodes received from remote node
     * @return the node resource of this node
     */
    public Map<Integer,NodeResource> resourceGossip(Map<Integer,NodeResource> receivedResourceNodes) {
        log.info("Remote node invoked member gossip resource with this node");
        Map<Integer,NodeResource> initialSet =(Map<Integer,NodeResource>)((HashMap<Integer,NodeResource>)nodeContext.getMemberResourceMap()).clone();
        Map<Integer,NodeResource> newSet = mergeNewNodeResource(initialSet, receivedResourceNodes);
        nodeContext.updateMemberResourceSet(newSet);
        return nodeContext.getPartialResourceNodes();
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
            TTransport transport = new TSocket(randomMember.getIp(), randomMember.getPort());
            //TTransport transport = new TFramedTransport(new TSocket("localhost",  randomMember.getPort()));
            try {
                transport.open();
                TProtocol protocol = new TBinaryProtocol(transport);
                Siyapath.Client client = new Siyapath.Client(protocol);
                Set<NodeInfo> nodesToGossip = getDiff(randomMember);
                Set<NodeInfo> discoveredNodes = CommonUtils.deSerialize(client.memberDiscovery(CommonUtils.serialize(nodeContext.getNodeInfo()), CommonUtils.serialize(nodesToGossip)));
                Set<NodeInfo> newSet = mergeSets(nodeContext.getMemberSet(), discoveredNodes);
                nodeContext.updateMemberSet(newSet);
                nodeContext.addMemNodeSet(randomMember, discoveredNodes);
                log.info("Members fetched " + discoveredNodes.size());
            } catch (SecurityException e) {
                log.error("Could not member gossip due to Security Exception");
                e.printStackTrace();
            } catch (TTransportException e) {
                if (e.getCause() instanceof ConnectException) {
                    log.error("Could not connect to the member node for member gossiping");
                    nodeContext.removeMember(randomMember);
                    nodeContext.removeMemNodeSet(randomMember);
                } else {
                    e.printStackTrace();
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
            TTransport transport = new TSocket(randomMember.getIp(), randomMember.getPort());
            //TTransport transport = new TFramedTransport(new TSocket("localhost",  randomMember.getPort()));
            try {
                transport.open();
                TProtocol protocol = new TBinaryProtocol(transport);
                Siyapath.Client client = new Siyapath.Client(protocol);
                Map<Integer,NodeResource> discoveredNodesResource = CommonUtils.deSerialize(client.resourceGossip(CommonUtils.serialize(nodeContext.getPartialResourceNodes())));
                Map<Integer,NodeResource> newSet = mergeNewNodeResource(nodeContext.getMemberResourceMap(), discoveredNodesResource);
                nodeContext.updateMemberResourceSet(newSet);
                log.info("Node Resource Fetched:" + discoveredNodesResource.size());

            } catch (SecurityException e) {
                log.error("Could not resource gossip due to Security Exception");
            } catch (TTransportException e) {
                if (e.getCause() instanceof ConnectException) {
                    log.error("Could not connect to the member node for resource gossiping");
                    nodeContext.removeMember(randomMember);
                    nodeContext.removeFromMemResourceMap(randomMember.getNodeId());
                } else {
                    e.printStackTrace();
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
        for (int i = tempSet.size(); i > 0 && newSet.size() > 0.5 * SiyapathConstants.MEMBER_SET_LIMIT; i--) {
            newSet.remove(newSet.iterator().next());
        }
        newSet.addAll(tempSet);
        return newSet;
    }

    /**
     * @param initialMap
     * @param discoveredResourceNodes
     * @return new merged Set of node resource
     */
    private synchronized Map<Integer, NodeResource> mergeNewNodeResource(Map initialMap, Map<Integer, NodeResource> discoveredResourceNodes) {
        Map<Integer, NodeResource> newMap = initialMap;

        for (Map.Entry<Integer, NodeResource> entry : discoveredResourceNodes.entrySet()) {
            int key = entry.getKey();
            NodeResource value = entry.getValue();
            if (key != nodeContext.getNodeInfo().getNodeId()) {
                if (newMap.size() < SiyapathConstants.RESOURCE_MEMBER_SET_LIMIT) {
                    if (initialMap.containsKey(key)) {
                        NodeResource initialResource = (NodeResource) initialMap.get(key);
                        Timestamp initialStamp = new Timestamp(initialResource.getTimeStamp());
                        Timestamp newStamp = new Timestamp(value.getTimeStamp());
                        if (initialStamp.before(newStamp)) {
                            newMap.put(key, value);
                        }
                    } else {
                        newMap.put(key, value);
                    }

                } else {
                    int newKey = newMap.entrySet().iterator().next().getKey();
                    if (initialMap.containsKey(key)) {
                        NodeResource initialResource = (NodeResource) initialMap.get(key);
                        Timestamp initialStamp = new Timestamp(initialResource.getTimeStamp());
                        Timestamp newStamp = new Timestamp(value.getTimeStamp());
                        if (initialStamp.before(newStamp)) {
                            newMap.put(key, value);
                        }
                    } else {
                        newMap.remove(newKey);
                        newMap.put(key, value);

                    }
                }
            }
        }

        return newMap;
    }

    private Set<NodeInfo> getDiff(NodeInfo gossipMember) {
        HashSet<NodeInfo> tempSet = new HashSet<NodeInfo>();
        HashSet<NodeInfo> initialSet = (HashSet<NodeInfo>) ((HashSet<NodeInfo>) nodeContext.getMemberSet()).clone();
        initialSet.add(nodeContext.getNodeInfo());
        HashSet<NodeInfo> memberSet = (HashSet<NodeInfo>) nodeContext.getMemNodeSet(gossipMember);
        if (memberSet != null) {
            for (NodeInfo newNode : initialSet) {
                if (!memberSet.contains(newNode)) {
                    tempSet.add(newNode);
                }
            }
            return tempSet;
        }
        return initialSet;
    }
}
