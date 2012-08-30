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