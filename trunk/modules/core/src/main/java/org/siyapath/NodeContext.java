package org.siyapath;

import org.siyapath.job.JobHandler;
import org.siyapath.utils.CommonUtils;

import javax.xml.bind.SchemaOutputResolver;
import javax.xml.stream.events.NotationDeclaration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The NodeContext holds runtime information
 * for a Siyapath node instance.
 */
public class NodeContext {

    public enum NodeStatus {
        CREATED,
        STARTING,
        LISTENING,
        PROCESSING
    }

    /**
     * This node's information
     */
    private NodeInfo nodeInfo;
    private ArrayList<JobHandler> jobHandlerList;
    /**
     * List of known member nodes
     */
    private HashSet<NodeInfo> members;
    private ConcurrentHashMap<NodeInfo, HashSet<NodeInfo>> memWithNodeSet;  //TODO: as of now uses concurrentHashmap ,also can provide synchronized block ,which is better?
    private boolean isBackup;
    private HashSet<NodeResource> memberResource;

    private NodeStatus nodeStatus;

    private NodeResource nodeResource;


    /**
     *
     */
    public NodeContext(NodeInfo nodeInfo) {
        this.members = new HashSet<NodeInfo>();
        this.memberResource = new HashSet<NodeResource>();
        this.jobHandlerList = new ArrayList<JobHandler>();
        this.memWithNodeSet = new ConcurrentHashMap<NodeInfo, HashSet<NodeInfo>>();
        this.nodeInfo = nodeInfo;
        nodeResource = new NodeResource(nodeInfo);
        nodeStatus = NodeStatus.CREATED;
    }

    public NodeStatus getNodeStatus() {
        return nodeStatus;
    }

    public void setNodeStatus(NodeStatus status) {
        nodeStatus = status;
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

    public void addMemNodeSet(NodeInfo member, Set<NodeInfo> nodeSet) {
        if (members.contains(member)) {
            this.memWithNodeSet.put(member, (HashSet<NodeInfo>) nodeSet);
        }
    }

    public void updateMemNodeSet() {  //TODO:uses concurrent Hashmap, Synchronized block?
        Iterator nodes = memWithNodeSet.keySet().iterator();
        while (nodes.hasNext()) {
            NodeInfo newNode = (NodeInfo) nodes.next();
            if (!members.contains(newNode)) {
                memWithNodeSet.remove(newNode);
            }
        }

    }

    public Set<NodeInfo> getMemNodeSet(NodeInfo member) {
        return this.memWithNodeSet.get(member);
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
        updateMemNodeSet();  //TODO:Concurrency Control
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
        return nodeResource.refreshProperties();
    }

    public void setNodeResource(NodeResource nodeResource) {
        this.nodeResource = nodeResource;
    }

    /**
     * @return information of this node
     */
    public NodeInfo getNodeInfo() {
        return nodeInfo;
    }

    /**
     * Sets this node's information
     *
     * @param nodeInfo node information
     */
    public void setNodeInfo(NodeInfo nodeInfo) {
        this.nodeInfo = nodeInfo;
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

    public void addJob(JobHandler jobHandler) {
        jobHandlerList.add(jobHandler);
    }

    public ConcurrentHashMap<NodeInfo, HashSet<NodeInfo>> getMemWithNodeSet() {
        return memWithNodeSet;
    }

    public void setMemWithNodeSet(ConcurrentHashMap<NodeInfo, HashSet<NodeInfo>> memWithNodeSet) {
        this.memWithNodeSet = memWithNodeSet;
    }

    public boolean isBackup() {
        return isBackup;
    }

    public void setBackup(boolean backup) {
        isBackup = backup;
    }

}
