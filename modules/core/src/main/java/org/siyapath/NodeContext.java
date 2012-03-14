package org.siyapath;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * The NodeContext holds runtime information
 * for a Siyapath node instance. This is a singleton
 */
public class NodeContext {

    /**
     * List of known member nodes
     */
    private HashSet<Integer> members;
    private boolean isBootstrapper;
    private boolean isBackup;
    private static NodeContext instance = null;
    private NodeData nodeData;

    private NodeContext() {
        this.members = new HashSet<Integer>();
        this.isBootstrapper = false;
        nodeData = new NodeData();
    }

    /**
     * Returns the instance of this class
     *
     * @return NodeContext instance
     */
    public static NodeContext getInstance() {
        if (instance == null) {
            instance = new NodeContext();
        }
        return instance;
    }

    /**
     * Returns a random member from list of known members
     *
     * @return a random member node
     */
    public Integer getRandomMember() {
        Integer randomMember = null;
        if (!getMemberSet().isEmpty()) {
            int memberCount = getMemberCount();
            int randomMemberID = new Random().nextInt(memberCount);
            int i = 0;
            for (Integer newRandomMember : this.getMemberSet()) {
                if (i == randomMemberID) {
                    randomMember = newRandomMember;
                    break;
                }
                i = i + 1;
            }
        }
        return randomMember;
    }

    /**
     * Adds a new member
     *
     * @param id member id
     */
    public void addMember(int id) {
        members.add(Integer.valueOf(id));
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
    public Set<Integer> getMemberSet() {
        return members;
    }

    /**
     * Replace the members set with a new set
     *
     * @param newSet the set to be replaced by
     */
    public void updateMemberSet(Set<Integer> newSet) {
        for (Integer newNode : newSet) {
            if (newNode == this.getNodeID()) {
                newSet.remove(newNode);
            }
        }
        members = (HashSet<Integer>) newSet;
    }

    /**
     * checks whether this node is the bootstrapper
     *
     * @return true or false
     */
    public boolean isBootstrapper() {
        return isBootstrapper;
    }

    /**
     * Declare this node as the bootstrapper
     *
     * @param bootstrapper true or false
     */
    public void setBootstrapper(boolean bootstrapper) {
        isBootstrapper = bootstrapper;
    }

    /**
     * Assign the ID for the node
     *
     * @param nodeID the assigned ID for the node
     */
    public void setNodeID(int nodeID) {
        this.nodeData.setNodeID(nodeID);
    }

    /**
     * Getter for NodeID
     *
     * @return nodeID
     */
    public int getNodeID() {
        return this.nodeData.getNodeID();
    }
}
