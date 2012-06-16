package org.siyapath.job;

import org.siyapath.NodeInfo;
import org.siyapath.service.Task;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 6/17/12
 * Time: 1:26 AM
 * To change this template use File | Settings | File Templates.
 */
public interface JobScheduler {

    public NodeInfo selectTaskProcessorNode(Task task);

}
