package org.siyapath;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.siyapath.job.BackupHandler;
import org.siyapath.job.JobProcessor;
import org.siyapath.service.NodeStatus;
import org.siyapath.utils.CommonUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The NodeContext holds runtime information
 * for a Siyapath node instance.
 */
public class NodeContext {

    //    private static final Log log = LogFactory.getLog(NodeContext.class);
    private JobProcessor jobProcessor;


    /**
     * This node's information
     */
    private NodeInfo nodeInfo;
//    private Map<Integer,JobHandler> jobHandlerMap;
//    private Map<Integer,TaskProcessor> taskProcessorMap;

    /**List of known member nodes*/
    private Set<NodeInfo> members;
    /**Map of nodeResources*/
    private Map<Integer, NodeResource> memberResource;
    /**Map of members with nodeInfo set*/
    private ConcurrentHashMap<NodeInfo, HashSet<NodeInfo>> memWithNodeSet;
    private boolean isBackup;
    private NodeResource nodeResource;
    private boolean presenceNotified;
    private boolean guiEnabled;
    private boolean listenerEnabled;
    private boolean workerEnabled;
    /** Number of currently tasks processing tasks*/
    private int processingTasksNo;
    private BackupHandler backupHandler;
    private Set taskIds;
    private int totalTasks;


    public NodeContext(NodeInfo nodeInfo) {
        this.taskIds = new HashSet();
        this.members = new HashSet<NodeInfo>();
        this.memberResource = new HashMap<Integer, NodeResource>();
//        this.taskProcessorMap = new HashMap<Integer, TaskProcessor>();
        this.memWithNodeSet = new ConcurrentHashMap<NodeInfo, HashSet<NodeInfo>>();
        this.nodeInfo = nodeInfo;
        nodeResource = new NodeResource(nodeInfo);
        processingTasksNo = 0;
        totalTasks=0;


    }

    /**
     *
     * @return processingTasksNo
     */
    public int getProcessingTasksNo() {
        return processingTasksNo;
    }

    /**
     * set processingTasksNo
     *
     * @param processingTasksNo
     */
    public synchronized void setProcessingTasksNo(int processingTasksNo) {
        this.processingTasksNo = processingTasksNo;
    }

    /**
     * Increase the number of  currently processing tasks
     */
    public synchronized void increaseProTasksNo(int taskId) {
        processingTasksNo++;
        if (this.getProcessingTasksNo() >= SiyapathConstants.PARALLEL_TASKS) {
            getNodeResource().setNodeStatus(NodeStatus.PROCESSING_BUSY);

        } else {
            getNodeResource().setNodeStatus(NodeStatus.PROCESSING_IDLE);
        }
        taskIds.add(taskId);
    }

    /**
     * Decrease the number of currently processing tasks
     */
    public synchronized void decreaseProTasksNo(int taskID) {
        if (processingTasksNo > 0) {
            processingTasksNo--;
            if (processingTasksNo == 0) {
                getNodeResource().setNodeStatus(NodeStatus.IDLE);
            } else if (this.getProcessingTasksNo() < SiyapathConstants.PARALLEL_TASKS) {
                getNodeResource().setNodeStatus(NodeStatus.PROCESSING_IDLE);
            }
            totalTasks++;
            taskIds.remove(taskID);
        }
    }

    public Set<Integer> getTaskIds() {
        return taskIds;
    }

    public int getTotalTasks() {
        return totalTasks;
    }

    public JobProcessor getJobProcessor() {
        if (jobProcessor == null) {
            jobProcessor = new JobProcessor(this); //lazy loading
        }
        return jobProcessor;
    }

    public BackupHandler getBackupHandler() {
        if (backupHandler == null) {
            backupHandler = new BackupHandler(this);
        }
        return backupHandler;
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
     * Returns a random member from list of known members
     *
     * @return randomMemberWithResource
     */
    public NodeResource getRandomMemberWithResource() {
        NodeResource randomMemberWithResource = null;
        int randomID = 0;
        if (!getMemberResourceMap().isEmpty()) {
            int memberCount = getMemberResourceMap().size();
            int randomIndex = CommonUtils.getRandomNumber(memberCount);
            Integer[] keyArray = getMemberResourceMap().keySet().toArray(new Integer[memberCount]);
            randomID = keyArray[randomIndex];
            randomMemberWithResource = getMemberResourceMap().get(randomID);
        }
        return randomMemberWithResource;
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
     * Remove a member from known members
     * @param member
     */
    public void removeMember(NodeInfo member) {
        members.remove(member);
    }

    /**
     * Add a new entry to the members with node set
     *
     * @param member
     * @param nodeSet
     */
    public void addMemNodeSet(NodeInfo member, Set<NodeInfo> nodeSet) {
        if (members.contains(member)) {
            this.memWithNodeSet.put(member, (HashSet<NodeInfo>) nodeSet);
        }
    }

    /**
     * Remove a entry from members with node set
     * @param member
     */
    public void removeMemNodeSet(NodeInfo member) {
        memWithNodeSet.remove(member);
    }

    /**
     * Update members with node set by removing members not known
     */
    public void updateMemNodeSet() {
        Iterator nodes = memWithNodeSet.keySet().iterator();
        while (nodes.hasNext()) {
            NodeInfo newNode = (NodeInfo) nodes.next();
            if (!members.contains(newNode)) {
                memWithNodeSet.remove(newNode);
            }
        }

    }

    /**
     * Get a member set of a given node
     *
     * @param member
     * @return member set
     */
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
        members = newSet;
        updateMemNodeSet();
    }

    /**
     * checks whether this node is the bootstrapper
     *
     * @return true or false
     */
    public boolean isBootstrapper() {
        return nodeInfo.isBootstrapper();
    }

    /**
     * Get NodeResource instance
     *
     * @return nodeResource
     */
    public NodeResource getNodeResource() {
        return nodeResource;
    }

    /**
     * Set nodeResource
     *
     * @param nodeResource
     */
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

    /**
     * Get memberResource Map
     *
     * @return memberResource
     */
    public Map<Integer, NodeResource> getMemberResourceMap() {
        return memberResource;
    }

    /**
     * remove a entry from resource Map
     * @param nodeID
     */
    public void removeFromMemResourceMap(int nodeID) {
        memberResource.remove(nodeID);
    }

    /**
     * Iterate though the nodeResource Map and selects a partial Map
     *
     * @return partial Map of nodeResource
     */
    public Map<Integer, NodeResource> getPartialResourceNodes() {
        int limit = (int) (SiyapathConstants.RESOURCE_MEMBER_SET_LIMIT * 0.25);

        Map<Integer, NodeResource> members = (HashMap<Integer, NodeResource>) ((HashMap<Integer, NodeResource>) getMemberResourceMap()).clone();
        Map<Integer, NodeResource> newMap = new HashMap<Integer, NodeResource>();
        if (members.size() < limit) {
            newMap = members;
            newMap.put(getNodeInfo().getNodeId(), getNodeResource().refreshNodeResource());

        } else {

            for (Map.Entry<Integer, NodeResource> entry : members.entrySet()) {
                limit--;
                Integer key = entry.getKey();
                NodeResource value = entry.getValue();
                newMap.put(key, value);
                if (limit == 1) {
                    newMap.put(getNodeInfo().getNodeId(), getNodeResource().refreshNodeResource());
                    break;
                }
            }

        }
        return newMap;
    }

    /**
     * @param memberResource
     */
    public void updateMemberResourceSet(Map<Integer, NodeResource> memberResource) {
        this.memberResource = (HashMap<Integer, NodeResource>) memberResource;
    }

//    public JobHandler getJobHandler(int jobId){
//        JobHandler jobHandler=null;
//
//        Set<Integer> jobIds = jobHandlerMap.keySet();
//
//        for(Integer id : jobIds){
//            if(jobId==id){
//                jobHandler = jobHandlerMap.get(jobId);
//                log.info("Right! Got the job!");
//            }else {
//                log.info("No such job submitted");
//            }
//        }
//        log.info("Returning.");
//        return jobHandler;
//    }

//    public TaskProcessor getTaskProcessor(int taskId){
//        TaskProcessor taskProcessor=null;
//
//        Set<Integer> taskIds = taskProcessorMap.keySet();
//
//        for(Integer id : taskIds){
//            if(taskId==id){
//                taskProcessor = taskProcessorMap.get(taskId);
//                log.info("Cool! Got the task!");
//            } else {
//                log.info("No such task distributed");
//            }
//        }
//        log.info("returning matched task");
//        return taskProcessor;
//    }

//    public void addJob(int jobId, JobHandler jobHandler) {
//        jobHandlerMap.put(jobId, jobHandler);
//    }

//    public void addTask(int taskId, TaskProcessor taskProcessor){
//        taskProcessorMap.put(taskId, taskProcessor);
//    }

    /**
     * Get members with nodeSet
     *
     * @return memWithNodeSet
     */
    public ConcurrentHashMap<NodeInfo, HashSet<NodeInfo>> getMemWithNodeSet() {
        return memWithNodeSet;
    }

    /**
     * Set members with nodeSet
     * @param memWithNodeSet
     */
    public void setMemWithNodeSet(ConcurrentHashMap<NodeInfo, HashSet<NodeInfo>> memWithNodeSet) {
        this.memWithNodeSet = memWithNodeSet;
    }

    public boolean isBackup() {
        return isBackup;
    }

    public void setBackup(boolean backup) {
        isBackup = backup;
    }

    public boolean isPresenceNotified() {
        return presenceNotified;
    }

    public void setPresenceNotified(boolean presenceNotified) {
        this.presenceNotified = presenceNotified;
    }

    public boolean isGuiEnabled() {
        return guiEnabled;
    }

    public void setGuiEnabled(boolean guiEnabled) {
        this.guiEnabled = guiEnabled;
    }

    public boolean isListenerEnabled() {
        return listenerEnabled;
    }

    public void setListenerEnabled(boolean listenerEnabled) {
        this.listenerEnabled = listenerEnabled;
    }

    public boolean isWorkerEnabled() {
        return workerEnabled;
    }

    public void setWorkerEnabled(boolean workerEnabled) {
        this.workerEnabled = workerEnabled;
    }
}
