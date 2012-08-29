package org.siyapath.job;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.siyapath.service.Job;
import org.siyapath.service.Task;

import java.util.Map;
import java.util.concurrent.BlockingQueue;

/**
 * Splits tasks for submitted job, puts tasks in queue to be sent to TaskProcessors
 */
class TaskCollector implements Runnable {
    private static final Log log = LogFactory.getLog(TaskCollector.class);
    private BlockingQueue<Task> taskQueue;     // or Deque? i.e. double ended queue
    private Map<Integer, ProcessingTask> taskMap;   // taskID mapped to ProcessingTask

    private Job job;

    TaskCollector(BlockingQueue<Task> taskQueue, Map<Integer, ProcessingTask> taskMap, Job job) {
        this.taskQueue = taskQueue;
        this.taskMap = taskMap;
        this.job = job;
    }

    @Override
    public void run() {
//            NodeInfo backup = createBackup(job);
        for (Task task : job.getTasks().values()) {
            try {
//                    task.setBackup(CommonUtils.serialize(backup));
                if (!taskMap.containsKey(task.getTaskID())) {
                    ProcessingTask processingTask = new ProcessingTask(job.getJobID(), task.getTaskID(), job.getReplicaCount(), task);

                    task.setTaskReplicaCount(job.getReplicaCount());      //required to set
                    taskMap.put(task.getTaskID(), processingTask);

                    int replicaCount = job.getReplicaCount();
                    for (int i = 0; i < replicaCount; i++) {
                        taskQueue.put(task);
                    }

                    log.debug("Added " + task.getTaskID() + " to queue.");
//                        processingTask.setBackupNode(backup);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();  //TODO: handle exception
            }
        }
        log.info("Added " + job.getTasks().size() + " tasks to the queue");
    }

}
