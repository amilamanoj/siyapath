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
import org.siyapath.service.Job;
import org.siyapath.service.NodeData;
import org.siyapath.service.Siyapath;
import org.siyapath.service.Task;
import org.siyapath.utils.CommonUtils;

import java.net.ConnectException;
import java.util.*;


/*
* Assuming the task distributing node has the .class sent by user saved on disk by this time
* TODO: select a member from member list to assign the task to.
* */
public class JobHandler {

    private static final Log log = LogFactory.getLog(JobHandler.class);

    private Map<NodeInfo, Task> taskProcessingNodes = null;
    private NodeContext context;
    //    private Map<Integer, Task> tasks = null;   //taskID and task
    private NodeInfo backupNode;
    private Job currentJob;
    private boolean overallStatus = false;


    public JobHandler(NodeContext nodeContext, int jobId, Map<Integer, Task> tasks) {
        //uses the default constructor at the sender non-requisition
        currentJob = new Job();
        currentJob.setJobID(jobId);
        currentJob.setTasks(tasks);
//        this.jobId = jobId;
        context = nodeContext;
//        this.tasks = tasks;
        taskProcessingNodes = new HashMap<NodeInfo, Task>();
    }

    public void startScheduling() {
        createBackup();
        log.info("Starting job:" + currentJob.getJobID());
        JobThread jobThread = new JobThread();
        jobThread.start();
    }

    private class JobThread extends Thread {

        @Override
        public void run() {

            Map<Integer, Task> tasks = currentJob.getTasks();
            JobScheduler scheduler = getJobScheduler();

            for (Task t : tasks.values()) {
                t.setBackup(CommonUtils.serialize(backupNode));
                NodeInfo selectedNode = scheduler.selectTaskProcessorNode(t);
                log.info("JobID:" + currentJob.getJobID() + "-TaskID:" + t.getTaskID() + "-Sending to: " + selectedNode);
                sendTask(t, selectedNode);
                if(taskProcessingNodes!=null){
                    taskProcessingNodes.put(selectedNode, t);
                }
                //else?-no

            }
        }
    }

    private PushJobScheduler getJobScheduler() {
        return new PushJobScheduler(context);
    }

    /**
     * Submits a task to a specified node
     *
     * @param task            task to submit
     * @param destinationNode node to submit to
     */
    public void sendTask(Task task, NodeInfo destinationNode) {            //changed

        NodeInfo nodeInfo = context.getNodeInfo();
        NodeData thisNode = CommonUtils.serialize(nodeInfo);
        task.setSender(thisNode);
        int jobId = currentJob.getJobID();

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

    //The arg jobID is to be used once multiple jobs are handled by same JobHandler node
    public boolean acquireJobProcessingStatus(int jobID){

        boolean jobStatus = false;
        int port;
        int taskID;
        Task task;

        Set<Map.Entry<NodeInfo,Task>> entrySet = taskProcessingNodes.entrySet();
//        log.info("map entryset done, next starting the for each =====8 acq job pro stats");

        for(Map.Entry<NodeInfo,Task> entry : entrySet){
            port = entry.getKey().getPort();
//            log.info("port of task processor guy : " + port );
//            taskID = entry.getValue().getTaskID();
            task = entry.getValue();
            log.info("got a task, going task level, task id is " + task.getTaskID());
            acquireTaskProcessingStatus(task, port);
            boolean isTaskCompleted = acquireTaskProcessingStatus(task, port);
            task.setTaskCompletionStatus(isTaskCompleted);

        }

        boolean dummy = true;
        for(Task task1 : taskProcessingNodes.values()){
            dummy = dummy && task1.isTaskCompletionStatus();
        }
        jobStatus = dummy;

        return jobStatus;

    }

    public boolean acquireTaskProcessingStatus(Task task, int port){

        TTransport transport = new TSocket("localhost", port);
        boolean taskStatus = false;
        int taskID = task.getTaskID();// null check - check service class if u actually have the taskID set
        try {
            log.info("Polling status of task through JobHandler node " + context.getNodeInfo().getNodeId() +
                    " for processing node on port: " + port + ". TaskID is " + taskID );
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            Siyapath.Client client = new Siyapath.Client(protocol);
//            log.info("leaving JH acqtaskPstatus for thriftcall ============11");
            taskStatus = client.getTaskStatusFromTaskProcessor(task, port);
//            log.info?
        } catch (TTransportException e) {
            if (e.getCause() instanceof ConnectException) {
                e.printStackTrace();
            }
        } catch (TException e) {
            e.printStackTrace();
        } finally {
            transport.close();
        }
        return taskStatus;
    }

    private class StatusPollThread extends Thread {

        public boolean isRunning = false;
        private int jobID;

        @Override
        public void run() {
            log.info("Started status poll thread at JH");

            isRunning = true;
            while (isRunning) {
                isRunning = !(acquireJobProcessingStatus(this.getJobID()));
                if(isRunning){
                    try {
                        sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }else{
                    stopPolling();
                    log.info("Completed task, Terminating thread.");
                }
            }
        }

        //        means job is completed
        public void stopPolling() {
            isRunning = false;
            overallStatus = true;
        }

        public int getJobID() {
            return jobID;
        }

        public void setJobID(int jobID) {
            this.jobID = jobID;
        }
    }

    public boolean thriftCall(int jobID){
        StatusPollThread statusPollThread = new StatusPollThread();
        statusPollThread.setJobID(jobID);
        log.info("Gonna start the status poll thread at Job handler");
        statusPollThread.start();
        return overallStatus;

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
}
