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
import org.siyapath.job.scheduling.PushJobScheduler;
import org.siyapath.service.*;
import org.siyapath.utils.CommonUtils;

import java.net.ConnectException;
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
                Task task = taskQueue.poll(10, TimeUnit.SECONDS);  // thread waits if the queue is empty.
                if (task != null) { // BlockingQueue.poll returns null if the queue is empty after the timeout.

                    log.info("Dispatching task: " + task.getTaskID() + " JobID: " + task.getJobID());

                    NodeInfo targetTaskProcessor = getJobScheduler().selectTaskProcessorNode(task);
                    while (taskMap.get(task.getTaskID()).getTaskStatusMap().containsKey
                            (targetTaskProcessor.getNodeId())) {
                        targetTaskProcessor = getJobScheduler().selectTaskProcessorNode(task);
                    }
                    boolean dispatched = dispatchTask(task, targetTaskProcessor);

                    if (!dispatched) {
                        generalExecutor.submit(new TaskReCollector(taskQueue, task));  // add the task back to the queue to be dispatched later
                    } else {
                        ProcessingTask pTask = taskMap.get(task.getTaskID());
                        pTask.addToStatusMap(targetTaskProcessor.getNodeId(), TaskStatus.DISPATCHING);
                    }
                }
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

            if (isDispatched) {
                ProcessingTask pTask = taskMap.get(task.getTaskID());
                pTask.addToStatusMap(destinationNode.getNodeId(), TaskStatus.PROCESSING);
                pTask.setTimeLastUpdated(System.currentTimeMillis());
            }
        } catch (TTransportException e) {
            e.printStackTrace();
            if (e.getCause() instanceof ConnectException) {
                log.warn("Could not connect to " + destinationNode + ",assign task to another.");
            }
            //TODO: handle exception to pick other node
        } catch (TException e) {
            e.printStackTrace();
        }
        return isDispatched;

    }

    private JobScheduler getJobScheduler() {
        return new PushJobScheduler(context);
    }
}
