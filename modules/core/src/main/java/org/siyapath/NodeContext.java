package org.siyapath;

import java.util.ArrayList;
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

    private NodeContext() {
        this.members = new HashSet<Integer>();
        this.isBootstrapper = false;
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
     * Returns a random member from list of know members
     *
     * @return a random member node
     */
    public Integer getRandomMember() {
        if (members.isEmpty()) {
            return null;
        }
        return members.iterator().next();
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
    public int getMemeberCount() {
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
     * @param newSet the set to be replaced by
     */
    public void updateMemberSet(Set<Integer> newSet){
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
}
