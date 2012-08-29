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
import org.siyapath.job.scheduling.PushJobScheduler;
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

    private Map<Integer, Task> dispatchedTasks;  //keeps dispatched tasks since they are removed from queue's head

    public JobProcessor(NodeContext nodeContext) {
        //uses the default constructor at the sender non-requisition
        context = nodeContext;
        taskDispatcherExecutor = Executors.newCachedThreadPool();
        taskCollectorExecutor = Executors.newCachedThreadPool();
        generalExecutor = Executors.newCachedThreadPool();

        taskQueue = new LinkedBlockingQueue<Task>(SiyapathConstants.TASK_QUEUE_CAPACITY);
        jobMap = new ConcurrentHashMap<Integer, Job>();
        taskMap = new ConcurrentHashMap<Integer, ProcessingTask>();
        dispatchedTasks = new ConcurrentHashMap<Integer, Task>();

//        new TaskTracker("TaskTracker-" + context.getNodeInfo().toString()).start();

        context.getNodeResource().setNodeStatus(NodeStatus.DISTRIBUTING);

    }

    public void addNewJob(Job job) {
        if (context.getNodeResource().getNodeStatus() == NodeStatus.IDLE) {  //TODO: need to reject adding jobs if this node is a processor
            context.getNodeResource().setNodeStatus(NodeStatus.DISTRIBUTING);
        }
        log.info("Adding new job:" + job.getJobID() + " to the queue");
        jobMap.put(job.getJobID(), job);
        taskCollectorExecutor.submit(new TaskCollector(job)); //TODO: handle future (return value)
        taskDispatcherExecutor.submit(new TaskDispatcher());
    }

    /**
     * Gets triggered when a result arrives from a task processor. (any replica)
     * @param result
     */
    public synchronized void taskResultReceived(final Result result) {
        log.info("Task results received. ID:" + result.getTaskID());
        ProcessingTask pTask = taskMap.get(result.getTaskID());

        pTask.addResult(result.getResults());
        pTask.setStatus(result.getProcessingNode().getNodeID(), TaskStatus.DONE);
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
        if(resultReceivedCount==taskReplicaCount){
            try {
                boolean validated = validateResults(result.getTaskID());  //use
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     *
     * @param taskId
     * @return true if all results of replicas are equal, false otherwise
     */
    public synchronized boolean validateResults(int taskId) throws InterruptedException {
        ProcessingTask pTask = taskMap.get(taskId);
        ArrayList<byte[]> resultList = pTask.getResultList();
        boolean isValid = false;

        if(!resultList.isEmpty()){
            byte[] firstResult = resultList.get(0);
            validate: for(byte[] resultArray : resultList){
                isValid = Arrays.equals(firstResult, resultArray);
                if(!isValid){
                    break validate;
                }
            }
            if(isValid){
                pTask.setValidatedResult(firstResult);
                taskMap.put(taskId, pTask);
                log.info("Result validated for Task:" + taskId);
            }else {
                taskQueue.put(dispatchedTasks.get(taskId));
                log.info("Result not validated, adding task back to queue. Task:" + taskId);
            }
        }
        return isValid;
    }

    /**
     * Splits tasks for submitted job, puts tasks in queue to be sent to TaskProcessors
     */
    class TaskCollector implements Runnable {

        private Job job;

        TaskCollector(Job job) {
            this.job = job;
        }

        @Override
        public void run() {
//            NodeInfo backup = createBackup(job);
            for (Task task : job.getTasks().values()) {
                try {
                    while (taskQueue.remainingCapacity() <= 1) {
                        Thread.sleep(100);
                    }
//                    task.setBackup(CommonUtils.serialize(backup));
                    if (!taskMap.containsKey(task.getTaskID())) {
                        ProcessingTask processingTask = new ProcessingTask(job.getJobID(),task.getTaskID(),job.getReplicaCount());

                        task.setTaskReplicaCount(job.getReplicaCount());      //required to set
                        taskMap.put(task.getTaskID(), processingTask);
                        taskQueue.put(task);
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

    class TaskReCollector implements Runnable {
        private Task task;

        TaskReCollector(Task task) {
            this.task = task;
        }

        public void run() {
            try {
                taskQueue.put(task);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class TaskDispatcher implements Runnable {

        private boolean active = true;

        @Override
        public void run() {
            while (active) {
                try {
                    if (taskQueue.isEmpty() && jobMap.isEmpty()) {
                        context.getNodeResource().setNodeStatus(NodeStatus.IDLE);
                    }
                    Task task = taskQueue.poll(10, TimeUnit.SECONDS);  // thread waits if the queue is empty.
                    if (task != null) { // BlockingQueue.poll returns null if the queue is empty after the timeout.

                        int replicaCount = task.getTaskReplicaCount();
                        ArrayList<Integer> dispatchedNodes = new ArrayList<Integer>();

                        //dispatches tasks for user-specified number of replicas
                        for (int i=0; i<replicaCount; i++){
                            log.info("Preparing to dispatch task: " + task.getTaskID() + " JobID: " + task.getJobID());

                            NodeInfo targetTaskProcessor = getJobScheduler().selectTaskProcessorNode(task);
                            if(i>0){
                                while (dispatchedNodes.contains(targetTaskProcessor.getNodeId())){
                                    targetTaskProcessor = getJobScheduler().selectTaskProcessorNode(task);
                                }
                            }
                            log.info("replicating round i:" + i + ", Task ID: "+ task.getTaskID() +
                                    ", dispatched Node ID:" + targetTaskProcessor.getNodeId());
                            dispatchedNodes.add(targetTaskProcessor.getNodeId());

                            ProcessingTask modifiedTask = taskMap.get(task.getTaskID());
                            modifiedTask.setStatus(targetTaskProcessor.getNodeId(), TaskStatus.DISPATCHING);
                            taskMap.put(task.getTaskID(), modifiedTask);

                            boolean dispatched = dispatchTask(task, targetTaskProcessor);

                            //todo: verify: even if one replicated task fails being dispatched, task will be back on end
                            //todo: of queue. may starve
                            if (!dispatched) {
                                generalExecutor.submit(new TaskReCollector(task));  // add the task back to the queue to be dispatched later
                            } else {
                                dispatchedTasks.put(task.getTaskID(), task);    // if task was dispatched successfully
                            }
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
                    pTask.setStatus(destinationNode.getNodeId(),TaskStatus.PROCESSING);
                    pTask.setTimeLastUpdated(System.currentTimeMillis());
                }
            } catch (TTransportException e) {

                if (e.getCause() instanceof ConnectException) {
                    log.warn("Could not connect to " + destinationNode + ",assign task to another.");
                } else{
                    log.warn(e.getMessage());
                }
                //TODO: handle exception to pick other node
            } catch (TException e) {
                e.printStackTrace();
            } finally {
                transport.close();
            }
            return isDispatched;
        }
    }

    private JobScheduler getJobScheduler() {
        return new PushJobScheduler(context);
    }

    /**
     *
     * @param taskId
     * @return one status for a task, after assessing statuses of all replicated tasks
     * For one task ID there will be many replicas with multiple statuses
     */
    public synchronized TaskStatus getTaskStatusOfAllReplicas(int taskId){

        TaskStatus overallTaskStatus = null;
        int counter = 0;
        ProcessingTask pTask = taskMap.get(taskId);
        Map<Integer, TaskStatus> taskStatusMap = pTask.getTaskStatusMap();

        label: for (TaskStatus taskStatus : taskStatusMap.values()){
            switch (taskStatus) {
                case DISPATCHING:
                    overallTaskStatus = TaskStatus.DISPATCHING;
                    break label;          // if at least one replica is at RECEIVED state, overall state is RECEIVED
                case PROCESSING:
                    overallTaskStatus = TaskStatus.PROCESSING;
                    break label;         // if at least one replica is at PROCESSING state, overall state is PROCESSING
                case DONE:
                    counter ++;
                    break;
            }
        }

        if (counter==pTask.getReplicaCount()){     //if all statues are DONE
            log.debug("All replicated tasks completed for task-" + taskId);
            overallTaskStatus = TaskStatus.DONE;
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
                new Thread() {    //TODO: use pooling
                    @Override
                    public void run() {
                        clearCompletedJob(jobId, backup);
                    }
                }.start();
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
//        private boolean isRunning = false;
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

    /*
    private class TaskStatusPollThread extends Thread {

        public boolean isRunning = false;
        private int jobID;

        @Override
        public void run() {

            isRunning = true;
            while (isRunning) {
                acquireJobProcessingStatus(this.getJobID());
                try {
                    sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void stopPolling() {
            isRunning = false;
        }

        public int getJobID() {
            return jobID;
        }

        public void setJobID(int jobID) {
            this.jobID = jobID;
        }
    }

    public void thriftCall(int jobID) {
        TaskStatusPollThread taskStatusPollThread = new TaskStatusPollThread();
        taskStatusPollThread.setJobID(jobID);
        taskStatusPollThread.start();
    }

//    private NodeInfo getProcessingNode() {
//        String res = null;
//    NodeInfo selectedMember = null;
//        TTransport transport = new TSocket("localhost", FrameworkInformation.BOOTSTRAP_PORT);
//        try {
//            log.info("Getting list of members from bootstrap");
//            transport.open();
//            TProtocol protocol = new TBinaryProtocol(transport);
//            Siyapath.Client client = new Siyapath.Client(protocol);
//            context.updateMemberSet(CommonUtils.deSerialize(client.getMembers()));
//            log.info("Number of members from bootstrapper: " + context.getMemberCount());
//            selectedMember = context.getRandomMember();
//        } catch (TTransportException e) {
//            if (e.getCause() instanceof ConnectException) {
//                res = "connecEx";
//                e.printStackTrace();
//            }
//        } catch (TException e) {
//            res = "tEx";
//            e.printStackTrace();
//        } finally {
//            transport.close();
//        }
//        log.info("Selected node: " + selectedMember);
//
//        return selectedMember;
//    }

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
*/
}
