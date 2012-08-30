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
import org.siyapath.job.scheduling.PushTaskScheduler;
import org.siyapath.job.scheduling.TaskScheduler;
import org.siyapath.service.*;
import org.siyapath.utils.CommonUtils;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

final class TaskDispatcher implements Runnable {

    private static final Log log = LogFactory.getLog(TaskDispatcher.class);

    private boolean active = true;

    private NodeContext context;
    private BlockingQueue<Task> taskQueue;     // or Deque? i.e. double ended queue
    private Map<Integer, Job> jobMap;               // jobID mapped to Job
    private Map<Integer, ProcessingTask> taskMap;   // taskID mapped to ProcessingTask
    private ExecutorService generalExecutor;

    /**
     * Constructor
     * @param context
     * @param taskQueue
     * @param jobMap
     * @param taskMap
     * @param generalExecutor
     */
    TaskDispatcher(NodeContext context, BlockingQueue<Task> taskQueue, Map<Integer, Job> jobMap, Map<Integer, ProcessingTask> taskMap, ExecutorService generalExecutor) {
        this.context = context;
        this.taskQueue = taskQueue;
        this.jobMap = jobMap;
        this.taskMap = taskMap;
        this.generalExecutor = generalExecutor;
    }

    @Override
    public void run() {
        while (active) {
            try {
                if (taskQueue.isEmpty() && jobMap.isEmpty()) {
                    context.getNodeResource().setNodeStatus(NodeStatus.IDLE);
                }
                Task task = taskQueue.poll(10, TimeUnit.SECONDS);
                if (task != null) {

                    log.info("Dispatching task: " + task.getTaskID() + " JobID: " + task.getJobID());

                    NodeInfo targetTaskProcessor = getJobScheduler().selectTaskProcessorNode(task);
//                    while (taskMap.get(task.getTaskID()).getTaskStatusMap().containsKey
//                            (targetTaskProcessor.getNodeId())) {
//                        targetTaskProcessor = getJobScheduler().selectTaskProcessorNode(task);
//                    }
                    ArrayList<NodeInfo> processors = new ArrayList<NodeInfo>();
                    for (ProcessingTask.TaskReplica taskReplica : taskMap.get(task.getTaskID())
                            .getTaskReplicaList()) {
//                        if(targetTaskProcessor.equals(taskReplica.getProcessingNode())) {
//
//                        }
                        processors.add(taskReplica.getProcessingNode());
                    }

                    while (processors.contains(targetTaskProcessor)) {
                        targetTaskProcessor = getJobScheduler().selectTaskProcessorNode(task);
                    }

                    boolean dispatched = dispatchTask(task, targetTaskProcessor);

                    if (!dispatched) {
                        generalExecutor.submit(new TaskReCollector(taskQueue, task));  // add the task back to the queue to be dispatched later
                    } else {
                        ProcessingTask pTask = taskMap.get(task.getTaskID());
                        ProcessingTask.TaskReplica taskReplica = pTask.getTaskReplicaList().get(task.getTaskReplicaIndex());
                        taskReplica.setProcessingNode(targetTaskProcessor);
                        taskReplica.setTaskStatus(TaskStatus.PROCESSING);
                    }
                }
                Thread.sleep(SiyapathConstants.TASK_DISPATCH_FREQUENCY_MILLIS);
            } catch (InterruptedException e) {
                e.printStackTrace();  //TODO: handle exception
            }

        }
    }

    /**
     * Submits a task to a specified node
     *
     * @param task            task to submit
     * @param destinationNode node to submit to
     */
    public synchronized boolean dispatchTask(Task task, NodeInfo destinationNode) {

        NodeInfo nodeInfo = context.getNodeInfo();
        NodeData thisNode = CommonUtils.serialize(nodeInfo);
        task.setSender(thisNode);
        int jobId = task.getJobID();

        log.info("JobID:" + jobId + "-TaskID:" + task.getTaskID() + "-Attempting to connect to selected task-processor: " + destinationNode);
        TTransport transport = new TSocket(destinationNode.getIp(), destinationNode.getPort());

        boolean isDispatched = false;
        try {
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            Siyapath.Client client = new Siyapath.Client(protocol);
            log.info("JobID:" + jobId + "-TaskID:" + task.getTaskID() + "-Submitting to: " + destinationNode);
            isDispatched = client.submitTask(task);
            log.info("JobID:" + jobId + "-TaskID:" + task.getTaskID() + "-Task successfully submitted: " + isDispatched);

        } catch (TTransportException e) {
            if (e.getCause() instanceof ConnectException) {
                log.warn("Could not connect to " + destinationNode + ",assign task to another.");
            } else {
                log.warn(e.getMessage());
            }
        } catch (TException e) {
            log.warn(e.getMessage());
        }
        return isDispatched;

    }

    private TaskScheduler getJobScheduler() {
        return new PushTaskScheduler(context);
    }
}
