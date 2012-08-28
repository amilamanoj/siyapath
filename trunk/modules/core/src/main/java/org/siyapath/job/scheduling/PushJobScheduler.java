package org.siyapath.job.scheduling;


import org.apache.commons.logging.LogFactory;
import org.siyapath.NodeContext;
import org.siyapath.NodeInfo;
import org.siyapath.NodeResource;
import org.siyapath.job.JobScheduler;
import org.siyapath.service.NodeStatus;
import org.siyapath.service.ResourceLevel;
import org.siyapath.service.Task;

import java.util.Map;


public class PushJobScheduler implements JobScheduler {
    private final org.apache.commons.logging.Log log = LogFactory.getLog(PushJobScheduler.class);
    private NodeContext context;


    public PushJobScheduler(NodeContext nodeContext) {
        context = nodeContext;
    }

    @Override
    public NodeInfo selectTaskProcessorNode(Task task) {

        //return getWithoutResourceMatching();

        //resource matching option
        String requiredResources = task.getRequiredResourceLevel().trim();
        return getResourceMatching(requiredResources);
    }


    /**
     * Invoked when task distributor needs to find a optimal node to
     * push the task depend on the required resources defined by user
     *
     * @param reqResources the required resources defined by user
     * @return the optimal node to push the task
     */

    private NodeInfo getResourceMatching(String reqResources) {
        ResourceLevel reqResLevel;
        NodeInfo selectedNode = null;

        if (reqResources.equalsIgnoreCase("low")) {
            reqResLevel = ResourceLevel.LOW;
        } else if (reqResources.equalsIgnoreCase("medium")) {
            reqResLevel = ResourceLevel.MEDIUM;
        } else if (reqResources.equalsIgnoreCase(("high"))) {
            reqResLevel = ResourceLevel.HIGH;
        } else {
            log.error("Unsupported Resource Level");
            reqResLevel = ResourceLevel.MEDIUM;
        }

        /*for (Map.Entry<Integer, NodeResource> newEntry : nodes.entrySet()) {
            NodeResource node = newEntry.getValue();
            if (reqResLevel == node.getResourceLevel() && (node.isIdle() || node.getNodeStatus() == NodeStatus.PROCESSING_IDLE)) {
                selectedNode = node.getNodeInfo();
                break;
            }
        } */

        for(int i=0;i<context.getMemberResourceMap().size();i++) {
              NodeResource node = context.getRandomMemberWithResource();
           if (reqResLevel == node.getResourceLevel() && (node.isIdle() || node.getNodeStatus() == NodeStatus.PROCESSING_IDLE)) {
                selectedNode = node.getNodeInfo();
                break;
            }
        }

         if (selectedNode == null) {
           selectedNode=getWithoutResourceMatching();
        }
        log.debug("Selected Node: " + selectedNode.getNodeId());
        return selectedNode;
    }

    private NodeInfo getWithoutResourceMatching() {
        NodeInfo selectedNode = null;

//        Map<Integer, NodeResource> nodes = context.getMemberResourceMap();
        /*for (Map.Entry<Integer, NodeResource> newEntry : nodes.entrySet()) {
            NodeResource node = newEntry.getValue();
            if (node.isIdle() || node.getNodeStatus() == NodeStatus.PROCESSING_IDLE) {
                selectedNode = node.getNodeInfo();
                break;
            }
        }*/

        for (int i = 0; i < context.getMemberResourceMap().size(); i++) {
            NodeResource node = context.getRandomMemberWithResource();
            if (node.isIdle() || node.getNodeStatus() == NodeStatus.PROCESSING_IDLE) {
                selectedNode = node.getNodeInfo();
                break;
            }
        }

        if (selectedNode == null) {
           NodeInfo randomMember=context.getRandomMemberWithResource().getNodeInfo();
            if(randomMember!=null){
               selectedNode=randomMember;
            }else{
               selectedNode=context.getRandomMember();
            }
        }
        log.debug("Selected Node: " + selectedNode.getNodeId());
        return selectedNode;
    }
}
