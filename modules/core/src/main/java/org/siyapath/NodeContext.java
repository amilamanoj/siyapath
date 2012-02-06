package org.siyapath;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: amila
 * Date: 2/6/12
 * Time: 11:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class NodeContext {

    private ArrayList<Integer> members;

    public NodeContext() {
        this.members = new ArrayList<Integer>();
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
}
