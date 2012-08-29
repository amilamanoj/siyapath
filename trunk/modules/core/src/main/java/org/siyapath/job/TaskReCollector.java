package org.siyapath.job;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.siyapath.service.Task;

import java.util.concurrent.BlockingQueue;

class TaskReCollector implements Runnable {
    private static final Log log = LogFactory.getLog(TaskReCollector.class);

    private Task task;
    private BlockingQueue taskQueue;

    TaskReCollector(BlockingQueue taskQueue, Task task) {
        this.taskQueue = taskQueue;
        this.task = task;
    }

    public void run() {
        try {
            taskQueue.put(task);
        } catch (InterruptedException e) {
            log.warn("Thread was interrupted while waiting to put task to task queue. " + e.getMessage());
        }
    }
}