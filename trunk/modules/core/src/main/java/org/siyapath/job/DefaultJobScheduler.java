package org.siyapath.job;

import org.siyapath.NodeContext;
import org.siyapath.NodeInfo;
import org.siyapath.service.Task;

public class DefaultJobScheduler implements JobScheduler{

    private NodeContext context;

    public DefaultJobScheduler(NodeContext context) {
        this.context = context;
    }

    @Override
    public NodeInfo selectTaskProcessorNode(Task task) {
         return context.getRandomMember();
    }
}
