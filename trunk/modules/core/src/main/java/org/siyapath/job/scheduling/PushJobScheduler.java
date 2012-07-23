package org.siyapath.job.scheduling;


import org.siyapath.NodeContext;
import org.siyapath.NodeInfo;
import org.siyapath.NodeResource;
import org.siyapath.SiyapathConstants;
import org.siyapath.job.JobScheduler;
import org.siyapath.service.Task;

import javax.xml.bind.SchemaOutputResolver;
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


    /**
     *Invoked when task distributor needs to find a optimal node to
     * push the task depend on the required resources defined by user
     *
     * @param reqResources the required resources defined by user
     * @return the optimal node to push the task
     */

    private NodeInfo getMatchingNode(String reqResources) {
        Set<NodeResource> nodes = context.getMemberResourceSet();

        Iterator<NodeResource> nodeItr = nodes.iterator();

        while (nodeItr.hasNext()) {
            NodeResource nodeResource = nodeItr.next();
            String resData = nodeResource.getNodeProperties().get(SiyapathConstants.CPU_INFO);
            if (resData.equalsIgnoreCase(reqResources) && nodeResource.getNodeInfo().isIdle()) {
                return nodeResource.getNodeInfo();
            }
        }
        return context.getRandomMember();
    }
}
