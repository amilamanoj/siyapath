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

import java.net.ConnectException;
import java.util.*;
import java.util.concurrent.*;

/**
 * This role is played by the immediate node that receives the job from
 * Siyapath user
 */
public final class JobProcessor {

    private static final Log log = LogFactory.getLog(JobProcessor.class);

    private ExecutorService taskDispatcherExecutor;
    private ExecutorService taskCollectorExecutor;
    private ExecutorService generalExecutor;

    private NodeContext context;
    private BlockingQueue<Task> taskQueue;     
    private Map<Integer, Job> jobMap;               // jobID mapped to Job
    private Map<Integer, ProcessingTask> taskMap;   // taskID mapped to ProcessingTask

    /**
     * Constructor
     * @param nodeContext
     */
    public JobProcessor(NodeContext nodeContext) {

        context = nodeContext;
        taskDispatcherExecutor = Executors.newCachedThreadPool();
        taskCollectorExecutor = Executors.newCachedThreadPool();
        generalExecutor = Executors.newCachedThreadPool();

        taskQueue = new LinkedBlockingQueue<Task>(SiyapathConstants.TASK_QUEUE_CAPACITY);
        jobMap = new HashMap<Integer, Job>();
        taskMap = new ConcurrentHashMap<Integer, ProcessingTask>();
        taskDispatcherExecutor.submit(new TaskDispatcher(nodeContext, taskQueue, jobMap, taskMap, generalExecutor));

        generalExecutor.submit(new TaskTracker(taskQueue, taskMap, generalExecutor));

        context.getNodeResource().setNodeStatus(NodeStatus.DISTRIBUTING);

    }

    /**
     * Adds new job sent by user to the job queue
     * 
     * @param job
     */
    public void addNewJob(Job job) {

        context.getNodeResource().setNodeStatus(NodeStatus.DISTRIBUTING);

        log.info("Adding new job:" + job.getJobID() + " to the queue");
        jobMap.put(job.getJobID(), job);
        taskCollectorExecutor.submit(new TaskCollector(taskQueue, taskMap, job, context)); 
    }

    /**
     * Gets triggered when a result arrives from a task processor. (from any replica)
     *
     * @param result
     */
    public synchronized void taskResultReceived(final Result result) {
        log.info("Task results received. ID:" + result.getTaskID());
        ProcessingTask pTask = taskMap.get(result.getTaskID());

        pTask.addResult(result.getResults());
        pTask.getTaskReplicaList().get(result.getTaskReplicaIndex()).setTaskStatus(TaskStatus.DONE);
        pTask.incrementResultReceivedCount();
        taskMap.put(result.getTaskID(), pTask);

        generalExecutor.submit(new Thread() {
            @Override
            public void run() {
                sendTaskResultToBackup(result);
            }
        });

        int resultReceivedCount = pTask.getResultReceivedCount();
        int taskReplicaCount = pTask.getReplicaCount();
        log.info("Task result received for " + resultReceivedCount + " replicas of same task");
        if (resultReceivedCount == taskReplicaCount) {
            try {
                validateResults(result.getTaskID());  
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Validates results comparing results from replicas
     * 
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
                for (int i = 0; i < pTask.getReplicaCount(); i++) {
                    pTask.getTaskReplicaList().get(i).setTaskStatus(TaskStatus.DISPATCHING);
                    generalExecutor.submit(new TaskReCollector(taskQueue,
                            pTask.getTask().deepCopy().setTaskReplicaIndex(i)));  // add the task back to the queue to be dispatched later
                }
            }
        }
        return isValid;
    }

    /**
     * Gives the overall status considering statuses of all replicas of the same task
     * For one task ID there may be many replicas with multiple statuses
     *
     * @param taskId
     * @return overall status for a task, after assessing statuses of all replicated tasks
     */
    public synchronized TaskStatus getTaskStatusOfAllReplicas(int taskId) {

        TaskStatus overallTaskStatus = null;
        int counter = 0;
        ProcessingTask pTask = taskMap.get(taskId);

        label:
        for (ProcessingTask.TaskReplica taskReplica : pTask.getTaskReplicaList()) {
            TaskStatus taskStatus = taskReplica.getTaskStatus();
            switch (taskStatus) {
                case DISPATCHING:
                    overallTaskStatus = TaskStatus.DISPATCHING;
                    break label;        // if at least one replica is at DISPATCHING state, overall state is DISPATCHING
                case PROCESSING:
                    overallTaskStatus = TaskStatus.PROCESSING;
                    break label;        // if at least one replica is at PROCESSING state, overall state is PROCESSING
                case DONE:
                    counter++;
                    break;
            }
            log.debug("Task status: " + taskId + " is: " + taskStatus);
            log.debug("task replica list size: " + pTask.getTaskReplicaList().size());
        }

        if (counter == pTask.getReplicaCount()) {     //if all statues are DONE, overall status is set to DONE
            log.debug("All replicated tasks completed for task-" + taskId);
            overallTaskStatus = TaskStatus.DONE;
        } else {
            log.debug("All replicated tasks not completed for task-" + taskId);
        }
        return overallTaskStatus;
    }

    /**
     * Gets all task results and task status of the job
     * 
     * @param jobId
     * @return task status map for the given JobId, with the mapping taskID to task completion status
     */
    public Map<Integer, TaskResult> getTaskResults(final int jobId) {

        Map<Integer, TaskResult> taskStatusMap = null;
        Job requestedJob = jobMap.get(jobId);
        if (requestedJob != null) {
            Set<Integer> taskIds = requestedJob.getTasks().keySet();
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
                backupNode = processingTask.getBackupNode();
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

    /**
     * Performs clearing up finished job
     * Removes the job from job map, task list from task map
     *
     * @param jobID
     * @param backup
     */
    private void clearCompletedJob(int jobID, NodeInfo backup) {
        log.info("The job: " + jobID + " is complete. Removing it from task map and job map");
        for (Integer taskID : jobMap.get(jobID).getTasks().keySet()) {
            taskMap.remove(taskID);
        }
        jobMap.remove(jobID);
        TTransport transport = new TSocket(backup.getIp(), backup.getPort());
        try {
            log.info("Connecting to backup node to end backup. JobID: " + jobID);
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            Siyapath.Client client = new Siyapath.Client(protocol);
            client.endBackup();
        } catch (TTransportException e) {
            log.warn(e.getMessage());
            log.warn("Cannot connect to backup node.");
        } catch (TException e) {
            log.warn(e.getMessage());
        } finally {
            transport.close();
        }
    }

    /**
     * Invoked when a task processor sends an update to notify liveliness
     *
     * @param taskID           ID of the task
     * @param taskReplicaIndex task replica number
     */
    public void taskUpdateReceived(int taskID, int taskReplicaIndex) {
        taskMap.get(taskID).getTaskReplicaList().get(taskReplicaIndex).setTimeLastUpdated(System.currentTimeMillis());
    }

    /**
     * Adds a map of tasks to taskMap of job processor.
     * Used by backup node when taking over job processing.
     *
     * @param mapOfTasks A map mapping taskID to ProcessingTask
     */
    void addTasksToTaskMap(Map<Integer, ProcessingTask> mapOfTasks) {
        taskMap.putAll(mapOfTasks);
    }


    private void sendTaskResultToBackup(Result result) {
        NodeInfo backupNode = taskMap.get(result.getTaskID()).getBackupNode();
        if (backupNode != null) {
            TTransport transport = new TSocket(backupNode.getIp(), backupNode.getPort());
            try {
                transport.open();
                TProtocol protocol = new TBinaryProtocol(transport);
                Siyapath.Client client = new Siyapath.Client(protocol);
                log.info("Sending received task result to backup node." + backupNode);
                client.sendTaskResultToBackup(result);

            } catch (TTransportException e) {
                log.warn(e.getMessage());
                if (e.getCause() instanceof ConnectException) {
                    log.warn("Backup Node is no longer available on port: " + backupNode);
                }
            } catch (TException e) {
                log.warn(e.getMessage());
            } finally {
                transport.close();
            }
        }
    }
}
