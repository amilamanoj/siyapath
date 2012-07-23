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
import org.siyapath.service.Job;
import org.siyapath.service.NodeData;
import org.siyapath.service.Siyapath;
import org.siyapath.service.Task;
import org.siyapath.utils.CommonUtils;
import org.siyapath.service.NodeStatus;

import java.net.ConnectException;
import java.util.concurrent.*;


public class JobProcessor {

    private static final Log log = LogFactory.getLog(JobProcessor.class);

    private ExecutorService taskDispatcherExecutor;
    private ExecutorService taskCollectorExecutor;

    //    private Map<NodeInfo, Task> taskProcessingNodes = null;
    private NodeContext context;

    private NodeInfo backupNode;

    private BlockingQueue<Task> taskQueue;     // or Deque? i.e. double ended queue

    public JobProcessor(NodeContext nodeContext) {
        //uses the default constructor at the sender non-requisition
        context = nodeContext;
        taskDispatcherExecutor = Executors.newFixedThreadPool(10);
        taskCollectorExecutor = Executors.newFixedThreadPool(10);
        taskQueue = new LinkedBlockingQueue<Task>(SiyapathConstants.TASK_QUEUE_CAPACITY);

        taskDispatcherExecutor.submit(new TaskDispatcher());
    }

    public void addNewJob(Job job) {
//        createBackup();
        log.info("Adding new job:" + job.getJobID() + " to the queue");
        taskCollectorExecutor.submit(new TaskCollector(job)); //TODO: handle future (return value)
    }

    class TaskCollector implements Runnable {

        private Job job;

        TaskCollector(Job job) {
            this.job = job;
        }

        @Override
        public void run() {
            for (Task task : job.getTasks().values()) {
                try {
                    taskQueue.put(task);
                } catch (InterruptedException e) {
                    e.printStackTrace();  //TODO: handle exception
                }
            }
            context.getNodeInfo().setNodeStatus(NodeStatus.BUSY);
            log.info("Added " + job.getTasks().size() + " tasks to the queue");
        }
    }

    class TaskDispatcher implements Runnable {

        private boolean active = true;

        @Override
        public void run() {
            while (active) {
                try {
                    Task task = taskQueue.poll(10, TimeUnit.SECONDS);
                    if (task != null) { // BlockingQueue.poll returns null if the queue is empty after the timeout.
                        log.info("Dispatching task: " + task.getTaskID() + " JobID: " + task.getJobID());
                        dispatchTask(task, getJobScheduler().selectTaskProcessorNode(task));
                    }
                    if (taskQueue.isEmpty()) {
                        context.getNodeInfo().setNodeStatus(NodeStatus.IDLE);
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
        public void dispatchTask(Task task, NodeInfo destinationNode) {

            NodeInfo nodeInfo = context.getNodeInfo();
            NodeData thisNode = CommonUtils.serialize(nodeInfo);
            task.setSender(thisNode);
            int jobId = task.getJobID();

            log.debug("JobID:" + jobId + "-TaskID:" + task.getTaskID() + "-Attempting to connect to selected task-processor: " + destinationNode);
            TTransport transport = new TSocket("localhost", destinationNode.getPort());

            try {
                transport.open();
                TProtocol protocol = new TBinaryProtocol(transport);
                Siyapath.Client client = new Siyapath.Client(protocol);
                log.debug("JobID:" + jobId + "-TaskID:" + task.getTaskID() + "-Submitting to: " + destinationNode);
                client.submitTask(task);
                log.debug("JobID:" + jobId + "-TaskID:" + task.getTaskID() + "-Successfully submitted task to processing node!");
            } catch (TTransportException e) {
                e.printStackTrace();
                if (e.getCause() instanceof ConnectException) {
                    log.warn("Could not connect to " + destinationNode + ",assign task to another.");
                }
                //TODO: handle exception to pick other node
            } catch (TException e) {
                e.printStackTrace();
            }

        }
    }

    private JobScheduler getJobScheduler() {
        return new PushJobScheduler(context);
    }

    /*
    private void createBackup() {
        NodeData thisNode = CommonUtils.serialize(context.getNodeInfo());

        boolean isBackupAccepted = false;
        int jobId = currentJob.getJobID();
        do {
            NodeInfo selectedNode = context.getRandomMember(); //TODO: improve selection
            TTransport transport = new TSocket("localhost", selectedNode.getPort());

            try {
                transport.open();
                TProtocol protocol = new TBinaryProtocol(transport);
                Siyapath.Client client = new Siyapath.Client(protocol);
                log.info("JobID:" + jobId + "-Requesting backup from" + selectedNode);
                isBackupAccepted = client.requestBecomeBackup(jobId, thisNode);
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
        } while (!isBackupAccepted);    //TODO: improve handling denials
    }
    */

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
