package org.siyapath.job.scheduling;

import org.siyapath.NodeInfo;
import org.siyapath.service.Task;

/**
 * Scheduling algorithm to select a node to dispatch a task
 */
public interface TaskScheduler {

    /**
     * Selects a suitable node to dispatch a task
     * @param task the task to be dispatched
     * @return node to dispatch the task to
     */
    public NodeInfo selectTaskProcessorNode(Task task);

}
