package org.siyapath.job.scheduling;


import org.siyapath.NodeContext;
import org.siyapath.NodeInfo;
import org.siyapath.NodeResource;
import org.siyapath.SiyapathConstants;
import org.siyapath.job.JobScheduler;
import org.siyapath.service.Task;

import java.util.Iterator;
import java.util.Set;

public class PushJobScheduler implements JobScheduler {
    NodeContext context;

    public PushJobScheduler(NodeContext nodeContext) {
        context = nodeContext;
    }

    @Override
    public NodeInfo selectTaskProcessorNode(Task task) {

        String requiredResources = task.getRequiredResources().trim();

        return getMatchingNode(requiredResources);
    }

    private NodeInfo getMatchingNode(String reqResources) {
        Set<NodeResource> nodes = context.getMemberResourceSet();

        Iterator<NodeResource> nodeItr = nodes.iterator();

        while (nodeItr.hasNext()) {
            NodeResource nodeResource = nodeItr.next();
            String resData = nodeResource.getNodeProperties().get(SiyapathConstants.CPU_INFO);
            if (resData.equalsIgnoreCase(reqResources)) {
                return nodeResource.getNodeInfo();
            }
        }
        return context.getRandomMember();
    }
}
