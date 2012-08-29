package org.siyapath.job.scheduling;

import org.siyapath.NodeContext;
import org.siyapath.NodeInfo;
import org.siyapath.service.Task;

/**
 * Provides random node selection for the Job Processor
 */
public class RandomTaskScheduler implements TaskScheduler {

    private NodeContext context;

    public RandomTaskScheduler(NodeContext context) {
        this.context = context;
    }

    @Override
    public NodeInfo selectTaskProcessorNode(Task task) {
         return context.getRandomMember();
    }
}
