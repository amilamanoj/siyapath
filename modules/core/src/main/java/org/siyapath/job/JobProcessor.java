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

import java.net.ConnectException;
import java.util.*;
import java.util.concurrent.*;


public final class JobProcessor {

    private static final Log log = LogFactory.getLog(JobProcessor.class);

    private ExecutorService taskDispatcherExecutor;
    private ExecutorService taskCollectorExecutor;
    private ExecutorService generalExecutor;

    private NodeContext context;
    private BlockingQueue<Task> taskQueue;     // or Deque? i.e. double ended queue
    private Map<Integer, Job> jobMap;               // jobID mapped to Job
    private Map<Integer, ProcessingTask> taskMap;   // taskID mapped to ProcessingTask


    public JobProcessor(NodeContext nodeContext) {
        //uses the default constructor at the sender non-requisition
        context = nodeContext;
        taskDispatcherExecutor = Executors.newCachedThreadPool();
        taskCollectorExecutor = Executors.newCachedThreadPool();
        generalExecutor = Executors.newCachedThreadPool();

        taskQueue = new LinkedBlockingQueue<Task>(SiyapathConstants.TASK_QUEUE_CAPACITY);
        jobMap = new HashMap<Integer, Job>();
        taskMap = new ConcurrentHashMap<Integer, ProcessingTask>();
        taskDispatcherExecutor.submit(new TaskDispatcher(nodeContext, taskQueue, jobMap, taskMap, generalExecutor));


//        new TaskTracker("TaskTracker-" + context.getNodeInfo().toString()).start();

        context.getNodeResource().setNodeStatus(NodeStatus.DISTRIBUTING);

    }

    public void addNewJob(Job job) {

        context.getNodeResource().setNodeStatus(NodeStatus.DISTRIBUTING);

        log.info("Adding new job:" + job.getJobID() + " to the queue");
        jobMap.put(job.getJobID(), job);
        taskCollectorExecutor.submit(new TaskCollector(taskQueue, taskMap, job)); //TODO: handle future (return value)
    }

    /**
     * Gets triggered when a result arrives from a task processor. (any replica)
     *
     * @param result
     */
    public synchronized void taskResultReceived(final Result result) {
        log.info("Task results received. ID:" + result.getTaskID());
        ProcessingTask pTask = taskMap.get(result.getTaskID());

        pTask.addResult(result.getResults());
        pTask.addToStatusMap(result.getProcessingNode().getNodeID(), TaskStatus.DONE);
        pTask.incrementResultReceivedCount();
        taskMap.put(result.getTaskID(), pTask);

//        new Thread() {  //TODO: use pooling
//            @Override
//            public void run() {
//                sendTaskResultToBackup(result);
//            }
//        }.start();

        int resultReceivedCount = pTask.getResultReceivedCount();
        int taskReplicaCount = pTask.getReplicaCount();
        log.info("Task result received for " + resultReceivedCount + " replicas of same task");
        if (resultReceivedCount == taskReplicaCount) {
            try {
                boolean validated = validateResults(result.getTaskID());  //use
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * @param taskId
     * @return true if all results of replicas are equal, false otherwise
     */
    public synchronized boolean validateResults(int taskId) throws InterruptedException {
        ProcessingTask pTask = taskMap.get(taskId);
        ArrayList<byte[]> resultList = pTask.getResultList();
        boolean isValid = false;

        if (!resultList.isEmpty()) {
            byte[] firstResult = resultList.get(0);
            validate:
            for (byte[] resultArray : resultList) {
                isValid = Arrays.equals(firstResult, resultArray);
                if (!isValid) {
                    break validate;
                }
            }
            if (isValid) {
                pTask.setValidatedResult(firstResult);
                taskMap.put(taskId, pTask);
                log.info("Result validated for Task:" + taskId);
            } else {
                log.info("Result validation failed, adding task back to queue. Task:" + taskId);
                generalExecutor.submit(new TaskReCollector(taskQueue, pTask.getTask()));  // add the task back to the queue to be dispatched later
            }
        }
        return isValid;
    }

    /**
     * @param taskId
     * @return one status for a task, after assessing statuses of all replicated tasks
     *         For one task ID there will be many replicas with multiple statuses
     */
    public synchronized TaskStatus getTaskStatusOfAllReplicas(int taskId) {

        TaskStatus overallTaskStatus = null;
        int counter = 0;
        ProcessingTask pTask = taskMap.get(taskId);
        Map<Integer, TaskStatus> taskStatusMap = pTask.getTaskStatusMap();

        label:
        for (TaskStatus taskStatus : taskStatusMap.values()) {
            switch (taskStatus) {
                case DISPATCHING:
                    overallTaskStatus = TaskStatus.DISPATCHING;
                    break label;          // if at least one replica is at RECEIVED state, overall state is RECEIVED
                case PROCESSING:
                    overallTaskStatus = TaskStatus.PROCESSING;
                    break label;         // if at least one replica is at PROCESSING state, overall state is PROCESSING
                case DONE:
                    counter++;
                    break;
            }
            log.debug("Task status: " + taskId + " is: " + taskStatus);
            log.debug("status map size: " + taskStatusMap.size());
        }

        if (counter == pTask.getReplicaCount()) {     //if all statues are DONE
//            log.debug("All replicated tasks completed for task-" + taskId);
            overallTaskStatus = TaskStatus.DONE;
        } else {
            log.debug("All replicated tasks not completed for task-" + taskId);

        }
        return overallTaskStatus;
    }

    /**
     * @param jobId
     * @return task status map for the given JobId, with the mapping taskID to task completion status
     */
    public Map<Integer, TaskResult> getTaskStatusesForJob(final int jobId) {

        Map<Integer, TaskResult> taskStatusMap = null;
        Job requestedJob = jobMap.get(jobId);
        if (requestedJob != null) {
            Set<Integer> taskIds = requestedJob.getTasks().keySet(); // task ids of the requested job: should be there tasks
            taskStatusMap = new HashMap<Integer, TaskResult>();
            NodeInfo backupNode = null;
            boolean jobComplete = true;
            for (Integer taskId : taskIds) {
                ProcessingTask processingTask = taskMap.get(taskId);
                if (processingTask == null) {
                    log.debug("Task map size:" + taskMap.size());
                    jobComplete = false;
                    taskStatusMap.put(taskId, new TaskResult(null, null));
                    continue;
                }

                TaskStatus overallTaskStatus = getTaskStatusOfAllReplicas(taskId);

                TaskResult taskResult = new TaskResult(overallTaskStatus, null);
                taskResult.setResults(processingTask.getValidatedResult());
                taskStatusMap.put(taskId, taskResult);
                if (overallTaskStatus != TaskStatus.DONE) {
                    jobComplete = false;
                }
                backupNode = processingTask.getBackupNode();          //no prob for me
            }

            if (jobComplete) {
                log.info("The job: " + jobId + " is complete.");
                final NodeInfo backup = backupNode;
                generalExecutor.submit(new Thread() {
                    @Override
                    public void run() {
                        clearCompletedJob(jobId, backup);
                    }
                });
            }
        }
        return taskStatusMap;
    }

    private void clearCompletedJob(int jobID, NodeInfo backup) {
        log.info("The job: " + jobID + " is complete. Removing it from task map and job map");
        for (Integer taskID : jobMap.get(jobID).getTasks().keySet()) {
            taskMap.remove(taskID);
        }
        jobMap.remove(jobID);
//        TTransport transport = new TSocket(backup.getIp(), backup.getPort());
//        try {
//            log.info("Connecting to backup node to end backup. JobID: " + jobID);
//            transport.open();
//            TProtocol protocol = new TBinaryProtocol(transport);
//            Siyapath.Client client = new Siyapath.Client(protocol);
//            client.endBackup();
//        } catch (TTransportException e) {
//            e.printStackTrace();
//            log.warn("Cannot connect to backup node.");
//        } catch (TException e) {
//            e.printStackTrace();
//        } finally {
//            transport.close();
//        }
    }

    public void taskUpdateReceived(int taskID) {
        taskMap.get(taskID).setTimeLastUpdated(System.currentTimeMillis());
    }

//    private class TaskTracker extends Thread {
//
//        boolean isRunning = false;
//
//        private TaskTracker(String name) {
//            super(name);
//        }
//
//        @Override
//        public void run() {
//            isRunning = true;
//            while (isRunning) {
//                for (ProcessingTask pTask : taskMap.values()) {
//                    long updateInterval = System.currentTimeMillis() - pTask.getTimeLastUpdated();
//                    if (pTask.getStatus() == TaskStatus.PROCESSING && updateInterval > SiyapathConstants
//                            .MAX_TASK_UPDATE_INTERVAL_MILLIS) {
//                        Task task = jobMap.get(pTask.getJobID()).getTasks().get(pTask.getTaskID());
//                        try {
//                            taskQueue.put(task);
//                            log.debug("Task: " + pTask.getTaskID() + " has no response from " +
//                                    "processor " + pTask.getProcessingNode() + ". Adding back to queue.");
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();    //TODO: handle exception
//                        }
//                    }
//                }
//                try {
//                    sleep(10000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

//    public Map<Integer, Task> getJobResult() {
//        if (jobMap.isEmpty() && taskQueue.isEmpty()) {
//            if (context.getNodeInfo().getNodeStatus() != NodeStatus.PROCESSING) {
//                context.getNodeInfo().setNodeStatus(NodeStatus.IDLE);  //TODO:If this is a processor node
//            }
//        }
//        return null;
//    }

    /**
     * Adds a map of tasks to taskMap of job processor.
     * Used by backup node when taking over job processing.
     *
     * @param mapOfTasks A map mapping taskID to ProcessingTask
     */
    void addTasksToTaskMap(Map<Integer, ProcessingTask> mapOfTasks) {
        taskMap.putAll(mapOfTasks);
    }


    private NodeInfo createBackup(Job job) {
        NodeData thisNode = CommonUtils.serialize(context.getNodeInfo());

        boolean isBackupAccepted;
        NodeInfo backupNode = null;
        int jobId = job.getJobID();
        //do {
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
                log.info("JobID:" + jobId + "-Backup request denied by" + selectedNode);
            }
        } catch (TTransportException e) {
            e.printStackTrace();
        } catch (TException e) {
            e.printStackTrace();
        }
        //} while (!isBackupAccepted);    //TODO: improve handling denials
        return backupNode;
    }

    private void sendTaskResultToBackup(Result result) {
        NodeInfo backupNode = taskMap.get(result.getTaskID()).getBackupNode();
        TTransport transport = new TSocket(backupNode.getIp(), backupNode.getPort());
        try {
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            Siyapath.Client client = new Siyapath.Client(protocol);
            log.info("Sending received task result to backup node." + backupNode);
            client.sendTaskResultToBackup(result);

        } catch (TTransportException e) {
            e.printStackTrace();
            if (e.getCause() instanceof ConnectException) {
                log.warn("Backup Node is no longer available on port: " + backupNode);
            }
        } catch (TException e) {
            e.printStackTrace();
        } finally {
            transport.close();
        }
    }

    //The arg jobID is to be used once multiple jobs are handled by same JobProcessor node
//    public void acquireJobProcessingStatus(int jobID) {
//
//        boolean jobStatus = false;
//        int port;
//        int taskID;
//
//        Set<Map.Entry<NodeInfo, Task>> entrySet = taskProcessingNodes.entrySet();
//
//        for (Map.Entry<NodeInfo, Task> entry : entrySet) {
//            port = entry.getKey().getPort();
//            taskID = entry.getValue().getTaskID();
//            acquireTaskProcessingStatus(port, taskID);
//        }
////
////        Set<NodeInfo> nodes = taskProcessingNodes.keySet();
////        for (NodeInfo nodeInfo : nodes){
////
////        }
//
//    }

//    public boolean acquireTaskProcessingStatus(int port, int taskID) {
//
//        TTransport transport = new TSocket("localhost", port);
//        boolean taskStatus = false;
//        try {
//            log.info("Polling status of task through JobProcessor node " + context.getNodeInfo().getNodeId() +
//                    " for processing node on port: " + port + ". TaskID is " + taskID);
//            transport.open();
//            TProtocol protocol = new TBinaryProtocol(transport);
//            Siyapath.Client client = new Siyapath.Client(protocol);
//            taskStatus = client.getTaskStatusFromTaskProcessor(taskID);
////            log.info?
//        } catch (TTransportException e) {
//            if (e.getCause() instanceof ConnectException) {
//                e.printStackTrace();
//            }
//        } catch (TException e) {
//            e.printStackTrace();
//        } finally {
//            transport.close();
//        }
//        return taskStatus;
//    }


//    public void sendResultToUserNode(){
//
//        TTransport transport = new TSocket("localhost",task.getSender().getPort());
//
//        try {
//            transport.open();
//            TProtocol protocol = new TBinaryProtocol(transport);
//            Siyapath.Client client = new Siyapath.Client(protocol);
//            log.info("Sending computed result back to User node." + task.getSender());
//            client.sendTaskResult(task);
//
//        } catch (TTransportException e) {
//            e.printStackTrace();
//            if(e.getCause() instanceof ConnectException){
//                log.warn("User is no longer available on port: " + task.getSender());
//            }
//        } catch (TException e) {
//            e.printStackTrace();
//        }
}
