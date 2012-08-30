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
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.siyapath.NodeContext;
import org.siyapath.NodeInfo;
import org.siyapath.SiyapathConstants;
import org.siyapath.service.*;
import org.siyapath.utils.CommonUtils;

import java.util.Map;
import java.util.concurrent.BlockingQueue;

/**
 * Splits tasks for submitted job, puts tasks in queue to be sent to TaskProcessors
 */
class TaskCollector implements Runnable {
    private static final Log log = LogFactory.getLog(TaskCollector.class);
    private BlockingQueue<Task> taskQueue;     // or Deque? i.e. double ended queue
    private Map<Integer, ProcessingTask> taskMap;   // taskID mapped to ProcessingTask
    private NodeContext context;

    private Job job;

    TaskCollector(BlockingQueue<Task> taskQueue, Map<Integer, ProcessingTask> taskMap, Job job,
                  NodeContext context) {
        this.taskQueue = taskQueue;
        this.taskMap = taskMap;
        this.job = job;
        this.context = context;
    }

    @Override
    public void run() {
        NodeInfo backup = createBackup(job);
        for (Task task : job.getTasks().values()) {
            try {
//                    task.setBackup(CommonUtils.serialize(backup));

                //taskMap would contain the task if this is starting from a backup
                ProcessingTask processingTask = taskMap.get(task.getTaskID());
                if (processingTask == null) {
                    processingTask = new ProcessingTask(job.getJobID(), task.getTaskID(),
                            job.getReplicaCount(), task);
                    taskMap.put(task.getTaskID(), processingTask);
                }
                processingTask.setBackupNode(backup);
                int replicaCount = job.getReplicaCount();

                //some tasks would already be finished if this is starting from a backup
                int finishedTaskReplicas = processingTask.getTaskReplicaList().size();
                for (int i = finishedTaskReplicas; i < replicaCount; i++) {
                    Task taskCopy = task.deepCopy();
                    taskCopy.setTaskReplicaIndex(i);
                    processingTask.addToTaskReplicaList(processingTask.new TaskReplica(TaskStatus.DISPATCHING));
                    taskQueue.put(taskCopy);
                }
                log.debug("Added " + task.getTaskID() + " to queue.");

            } catch (InterruptedException e) {
                log.warn(e.getMessage());   //TODO: handle exception
            }
        }
        log.info("Added " + job.getTasks().size() + " tasks to the queue");
    }

    private NodeInfo createBackup(Job job) {
        NodeData thisNode = CommonUtils.serialize(context.getNodeInfo());

        boolean isBackupAccepted = false;
        NodeInfo backupNode = null;
        int jobId = job.getJobID();
        for (int i = 0; !isBackupAccepted && i < SiyapathConstants.MEMBER_SET_LIMIT; i++) {
            NodeInfo selectedNode = context.getRandomMember(); //TODO: improve selection
            TTransport transport = new TSocket("localhost", selectedNode.getPort());

            try {
                transport.open();
                TProtocol protocol = new TBinaryProtocol(transport);
                Siyapath.Client client = new Siyapath.Client(protocol);
                log.info("JobID:" + jobId + "-Requesting backup from" + selectedNode);
                isBackupAccepted = client.requestBecomeBackup(job, thisNode);
                if (isBackupAccepted) {
                    log.info("JobID:" + jobId + "-Backup request accepted by" + selectedNode);
                    backupNode = selectedNode;
                } else {
                    log.debug("JobID:" + jobId + "-Backup request denied by" + selectedNode);
                }
            } catch (TTransportException e) {
                log.warn(e.getMessage());
            } catch (TException e) {
                log.warn(e.getMessage());
            }
        }
        return backupNode;
    }

}
