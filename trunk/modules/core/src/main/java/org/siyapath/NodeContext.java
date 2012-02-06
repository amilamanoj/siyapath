package org.siyapath;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: amila
 * Date: 2/6/12
 * Time: 11:39 PM
 * To change this template use File | Settings | File Templates.
 */
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

    public Set<String> getMemberSet() {
        Set<String> nodeSet = new HashSet<String>();
        for (Integer node : members){
             nodeSet.add(node.toString());
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
