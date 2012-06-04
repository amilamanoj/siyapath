package org.siyapath;

import org.siyapath.utils.CommonUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * The NodeContext holds runtime information
 * for a Siyapath node instance.
 */
public class NodeContext {

    /**
     * This node's information
     */
    private NodeInfo nodeInfo;
    /**
     * List of known member nodes
     */
    private HashSet<NodeInfo> members;
    private boolean isBackup;
    private HashSet<NodeResource> memberResource;

    /**
     * @return information of this node
     */
    public NodeInfo getNodeInfo() {
        return nodeInfo;
    }

    /**
     * Sets this node's information
     * @param nodeInfo node information
     */
    public void setNodeInfo(NodeInfo nodeInfo) {
        this.nodeInfo = nodeInfo;
    }
    private NodeResource nodeResource;

    /**
     *
     */
    public NodeContext() {
        this.members = new HashSet<NodeInfo>();
        this.memberResource = new HashSet<NodeResource>();
        nodeInfo = new NodeInfo();
        nodeResource = new NodeResource(nodeInfo);

    }

    /**
     * Returns a random member from list of known members
     *
     * @return a random member node
     */
    public NodeInfo getRandomMember() {
        NodeInfo randomMember = null;
        if (!getMemberSet().isEmpty()) {
            int memberCount = getMemberCount();
            int randomIndex = CommonUtils.getRandomNumber(memberCount);
            NodeInfo[] memberArray = members.toArray(new NodeInfo[memberCount]);
            randomMember = memberArray[randomIndex];
        }
        return randomMember;
    }

    /**
     * Adds a new member
     *
     * @param member member information
     */
    public void addMember(NodeInfo member) {
        members.add(member);
    }

    /**
     * Returns the size of member list
     *
     * @return number of members
     */
    public int getMemberCount() {
        return members.size();
    }

    /**
     * Returns whether members exist or not
     *
     * @return true or false
     */
    public boolean membersExist() {
        return (members.size() > 0);
    }

    /**
     * Returns the the set of member
     *
     * @return member set
     */
    public Set<NodeInfo> getMemberSet() {
        return members;
    }

    /**
     * Replace the members set with a new set
     *
     * @param newSet the set to be replaced by
     */
    public void updateMemberSet(Set<NodeInfo> newSet) {
        members = (HashSet<NodeInfo>) newSet;
    }

    /**
     * checks whether this node is the bootstrapper
     *
     * @return true or false
     */
    public boolean isBootstrapper() {
        return nodeInfo.isBootstrapper();
    }

    public NodeResource getNodeResource() {
        return nodeResource;
    }

    public void setNodeResource(NodeResource nodeResource) {
        this.nodeResource = nodeResource;
    }

    /**
     * Declare this node as the bootstrapper
     *
     * @param bootstrapper true or false
     */
    public void setBootstrapper(boolean bootstrapper) {
        nodeInfo.setBootstrapper(bootstrapper);
    }

    public HashSet<NodeResource> getMemberResourceSet() {
        return memberResource;
    }

    /**
     * @param memberResource
     */
    public void updateMemberResourceSet(HashSet<NodeResource> memberResource) {
        this.memberResource = memberResource;
    }

    /**
     * Assign the ID for the node
     *
     * @param nodeID the assigned ID for the node
     */
    public void setNodeID(int nodeID) {
        nodeInfo.setNodeId(nodeID);
    }

    /**
     * Getter for NodeID
     *
     * @return nodeID
     */
    public int getNodeID() {
        return nodeInfo.getNodeId();
    }
}
