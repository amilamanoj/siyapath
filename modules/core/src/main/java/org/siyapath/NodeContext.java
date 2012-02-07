package org.siyapath;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;


public class NodeContext {

    private ArrayList<Integer> members;
    private boolean isBootstrapper;

    public NodeContext() {
        this.members = new ArrayList<Integer>();
        this.isBootstrapper = false;
    }

    public Integer getMember(int index) {
        return members.get(index);
    }

    public Integer getRandomMember() {
        if (members.isEmpty()) {
            return null;
        }
        return members.get(new Random().nextInt(members.size()));
    }

    public void addMember(int port) {
        members.add(new Integer(port));
    }

    public int getMemeberCount() {
        return members.size();
    }

    public boolean membersExist() {
        return (members.size() > 0) ? true : false;
    }

    public Set<Integer> getMemberSet() {
        Set<Integer> nodeSet = new HashSet<Integer>();
        for (Integer nodeID : members){
             nodeSet.add(nodeID);
        }
        return nodeSet;
    }

    public boolean isBootstrapper() {
        return isBootstrapper;
    }

    public void setBootstrapper(boolean bootstrapper) {
        isBootstrapper = bootstrapper;
    }
}
