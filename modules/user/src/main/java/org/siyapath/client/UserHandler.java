package org.siyapath.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.siyapath.*;
import org.siyapath.service.NodeStatus;
import org.siyapath.task.SiyapathTask;
import org.siyapath.service.Job;
import org.siyapath.service.Siyapath;
import org.siyapath.service.Task;
import org.siyapath.utils.CommonUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.text.SimpleDateFormat;
import java.util.*;

public class UserHandler {
    private static final Log log = LogFactory.getLog(UserHandler.class);

    private NodeContext context;
//    private NodeInfo jobHandlerNode;
    private NodeInfo clientEnd;
    private Map<Integer, JobData> jobMap = new HashMap<Integer, JobData>();
    private int jobId;
    private String jobIdString;
//    private boolean jobStatus = false;

    Map<Integer, String> taskCompletionDataMap;
    private Vector<Vector<String>> allRows = new Vector<Vector<String>>();
    Vector<String> eachRow = null;

    public UserHandler() {
        this.clientEnd = new NodeInfo();
        this.context = new NodeContext(clientEnd);
    }

    public int getJobId() {
        return jobId;
    }

    /**
     * Creates a unique jobID by appending ip and timestamp and a random number
     *
     * @return the created jobID
     */
    public int generateJobID() {
        String ip = context.getNodeInfo().getIp();
        String timestamp = new SimpleDateFormat("yyyy.MM.dd_HH:mm:ss.S").format(new Date());
        int random = CommonUtils.getRandomNumber(1000);
        jobIdString = ip + "::" + timestamp + "::" + random;
        jobId = Math.abs(jobIdString.hashCode());
        return jobId;
    }

    /**
     * @param username
     * @param password
     * @return String success if user can be authenticated, failure otherwise, or exception string
     */
    public String authenticate(String username, String password) {
        String res = null;
        TTransport transport = new TSocket(CommonUtils.getBootstrapperIP(), CommonUtils.getBootstrapperPort());
        try {
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            Siyapath.Client client = new Siyapath.Client(protocol);
            res = client.userLogin(username, password);
        } catch (TTransportException e) {
            if (e.getCause() instanceof ConnectException) {
                res = "connecEx";
                log.error(e.getMessage());
            }
        } catch (TException e) {
            res = "tEx";
            e.printStackTrace();
        } finally {
            transport.close();
        }
        return res;
    }

//    public void addJob(Map<String, TaskData> taskList){
//        int jobId = this.generateJobID();
//    }

    /**
     * Prepares the new job, selects a distributor nodes and sends the job
     *
     * @param taskList list of tasks
     */
    public void submitJob(String name, Map<String, TaskData> taskList) throws SubmissionFailedException {
        NodeInfo selectedNode = null;
        Job job = null;
        try {
            job = createJob(taskList);
            selectedNode = getDistributorNode();
            JobData jobData = new JobData(job.jobID, name, job, selectedNode);
            jobMap.put(jobData.getId(), jobData);

            sendJob(job, selectedNode);
        } catch (Exception e) {
            throw new SubmissionFailedException("Could not submit the job", e);
        }
    }

    private Job createJob(Map<String, TaskData> taskList) throws IOException {
        int jobId = this.generateJobID();
        int taskCounter = 0;
        Map<Integer, Task> taskMap = new HashMap<Integer, Task>();

        for (TaskData taskData : taskList.values()) {
            int taskId = Math.abs((jobIdString + "::" + taskCounter++).hashCode());
            Task task = createTask(taskId, taskData.getClassFile(), taskData.getInputData(), taskData.getRequiredResources());
            taskMap.put(taskId, task);
        }

        return new Job(jobId, CommonUtils.serialize(context.getNodeInfo()), taskMap);
    }

    /**
     * Creates and adds a new task to the job given a task class
     *
     * @param taskProgramFile class for the task to be created
     * @param inputData       input data
     */
    private Task createTask(int taskId, File taskProgramFile, String inputData,
                            String requiredResources) throws IOException {
        //TODO: implement assigning taskID, jobID. Client will ask JobScheduler/Handler for next available jobID
        Task task = new Task(taskId, jobId, CommonUtils.convertFileToByteBuffer
                (taskProgramFile.getAbsolutePath()), inputData, getJobInterfaceName(),
                CommonUtils.serialize(context.getNodeInfo()), null, requiredResources);
        return task;

    }

    /**
     * Selects a volunteer node that will act as the job distributor
     *
     * @return Node information of the selected node
     */
    private NodeInfo getDistributorNode() throws TException {

        NodeInfo selectedMember = null;
        TTransport transport = new TSocket(CommonUtils.getBootstrapperIP(), CommonUtils.getBootstrapperPort());
        try {
            log.info("Getting list of members from bootstrap");
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            Siyapath.Client client = new Siyapath.Client(protocol);
            context.updateMemberSet(CommonUtils.deSerialize(client.getMembers()));
            log.info("Number of members from bootstrapper: " + context.getMemberCount());
//        } catch (TTransportException e) {
//            if (e.getCause() instanceof ConnectException) {
//                e.printStackTrace();
//            }
//        } catch (TException e) {
//            e.printStackTrace();
        } finally {
            transport.close();
        }
        log.info("Selected node: " + selectedMember);

        for (NodeInfo nodeInfo : context.getMemberSet()) {
            if (nodeInfo.isIdle() || nodeInfo.getNodeStatus() == NodeStatus.DISTRIBUTING) {
                selectedMember = nodeInfo;
                break;
            }
        }

        return selectedMember;
    }

    /**
     * Sends the job to specified node
     *
     * @param node destination node
     */
    private void sendJob(Job job, NodeInfo node) throws TException {
        TTransport transport = new TSocket(node.getIp(), node.getPort());
        log.info("Sending new job to node: " + node);
        try {
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            Siyapath.Client client = new Siyapath.Client(protocol);
            client.submitJob(job);
//        } catch (TTransportException e) {
//            if (e.getCause() instanceof ConnectException) {
//                e.printStackTrace();
//            }
//        } catch (TException e) {
//            e.printStackTrace();
        } finally {
            transport.close();
        }
    }


    private String getJobInterfaceName() {
        return SiyapathTask.class.getName();
    }


    public void updateTableDataVectors() {

        if (!allRows.isEmpty()) {
            allRows.removeAllElements();
        }
        for (Map.Entry<Integer, String> task : taskCompletionDataMap.entrySet()) {
            eachRow = new Vector<String>();
            eachRow.add(task.getKey().toString());
            eachRow.add(task.getValue());
            allRows.add(eachRow);
        }
    }

    /**
     * Starts a new thread for polling status from JobProcessor
     */
    public void startPollingThread(int jobId, JTable table) {
        JobStatusPollThread jobStatusPollThread = new JobStatusPollThread(jobId, table);
        jobStatusPollThread.start();
    }

    public Vector<Vector<String>> getAllRows() {
        return allRows;
    }

    /**
     * Thread runs while job status is false, i.e. Job is incomplete
     */
    private class JobStatusPollThread extends Thread {

        int jobId;
        boolean jobStatus;
        int count;
        JTable table;

        JobStatusPollThread(int jobId, JTable table) {
            this.jobId = jobId;
            this.table = table;
        }

        @Override
        public void run() {
            log.info("Polling thread started for JobId: " + jobId);

            jobStatus = false;

            while (!jobStatus) {
                count++;
                pollStatusFromJobProcessor(jobId);
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * Contacts back the selected JobProcessor to get job status
         *
         * @param jobID
         */
        public void pollStatusFromJobProcessor(int jobID) {

            NodeInfo jobHandler = jobMap.get(jobID).getDistributorNode();
            TTransport transport = new TSocket(jobHandler.getIp(),
                    jobHandler.getPort());

            try {
                log.info("Polling status of job: " + jobID + " count: " + count);
                transport.open();
                TProtocol protocol = new TBinaryProtocol(transport);
                Siyapath.Client client = new Siyapath.Client(protocol);
                //gets the map of task statuses from JobProcessor
                //Maps each taskId to its processing status <Integer,String>
                taskCompletionDataMap = client.getJobStatus(jobID);
                //sets vectors to be fed to Status UI
                updateTableDataVectors();
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                model.fireTableDataChanged();
                jobStatus = assessJobStatusFromTaskStatuses(taskCompletionDataMap);

            } catch (TTransportException e) {
                if (e.getCause() instanceof ConnectException) {
                    e.printStackTrace();
                }
            } catch (TException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                transport.close();
            }
        }

        public boolean assessJobStatusFromTaskStatuses(Map<Integer, String> statusMap) {

            boolean statusCondition = true;
            boolean eachTaskStatus;

            for (String taskStatus : statusMap.values()) {
                if (taskStatus.equalsIgnoreCase("DONE")) {
                    eachTaskStatus = true;
                } else {
                    eachTaskStatus = false;
                }

                if (statusCondition && eachTaskStatus) statusCondition = true;
                else statusCondition = false;
            }
            return statusCondition;
        }

    }


//    public void demo(){
//        JobStatusPollThread thread = new JobStatusPollThread();
//        thread.start();
////        log.info("Started Job Status Polling thread.===1");
////        int count =0;
////        while (!jobStatus) {
////            log.info("Job status before polling tis time is " + jobStatus);
////            pollStatusFromJobProcessor(jobId);
////            log.info("Job status after polling tis time is " + jobStatus);
////            count++;
////            log.info("Polled iteration: " + count);
////
////        }
//
//    }

////        DEMO only
//    public static void main(String[] args) {
//        UserHandler userHandler = new UserHandler();
////    JobStatusPollThread thread = new JobStatusPollThread()
//        userHandler.demo();
//    }

}
