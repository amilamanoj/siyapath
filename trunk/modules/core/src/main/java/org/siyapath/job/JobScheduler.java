package org.siyapath.job;

import org.siyapath.NodeInfo;
import org.siyapath.service.Task;

public interface JobScheduler {

    public NodeInfo selectTaskProcessorNode(Task task);

}
