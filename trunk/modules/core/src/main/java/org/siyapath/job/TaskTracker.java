/*
 * Distributed under the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.siyapath.job;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.siyapath.SiyapathConstants;
import org.siyapath.service.Task;
import org.siyapath.service.TaskStatus;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

public class TaskTracker implements Runnable {
    private static final Log log = LogFactory.getLog(TaskTracker.class);

    private BlockingQueue<Task> taskQueue;             // jobID mapped to Job
    private Map<Integer, ProcessingTask> taskMap;   // taskID mapped to ProcessingTask
    private ExecutorService generalExecutor;

    public TaskTracker(BlockingQueue<Task> taskQueue, Map<Integer, ProcessingTask> taskMap, ExecutorService generalExecutor) {
        this.taskQueue = taskQueue;
        this.taskMap = taskMap;
        this.generalExecutor = generalExecutor;
    }

    boolean isRunning = false;

    @Override
    public void run() {
        isRunning = true;
        while (isRunning) {
            for (ProcessingTask pTask : taskMap.values()) {
                for (int i = 0; i < pTask.getTaskReplicaList().size(); i++) {
                    ProcessingTask.TaskReplica taskReplica = pTask.getTaskReplicaList().get(i);
                    long updateInterval = System.currentTimeMillis() - taskReplica.getTimeLastUpdated();
                    if (taskReplica.getTaskStatus() == TaskStatus.PROCESSING && updateInterval >
                            SiyapathConstants.MAX_TASK_UPDATE_INTERVAL_MILLIS) {
                        Task task = pTask.getTask().deepCopy().setTaskReplicaIndex(i);
                        generalExecutor.submit(new TaskReCollector(taskQueue, task));
                        log.debug("Task: " + pTask.getTaskID() + " has no response from " +
                                "processor " + taskReplica.getProcessingNode() + ". Adding back to queue.");
                    }
                }
            }
            try {
                Thread.sleep(SiyapathConstants.TASK_TRACKER_INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}