package org.siyapath.job;

import org.siyapath.NodeContext;
import org.siyapath.NodeInfo;
import org.siyapath.service.Task;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 6/17/12
 * Time: 1:30 AM
 * To change this template use File | Settings | File Templates.
 */
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
