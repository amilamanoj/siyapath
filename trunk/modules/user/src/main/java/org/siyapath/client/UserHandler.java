package org.siyapath.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.siyapath.FrameworkInformation;
import org.siyapath.NodeContext;
import org.siyapath.NodeInfo;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class UserHandler {
    private static final Log log = LogFactory.getLog(UserHandler.class);

    private NodeContext context;
    private NodeInfo jobHandlerNode;
    private NodeInfo clientEnd;
    private Map taskList = new HashMap<Integer, Task>();   //TODO: needed? nh
    private int jobId;
    private String jobIdString;
//    private boolean jobStatus = false;

    Map<Integer,String> taskCompletionDataMap;
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
        TTransport transport = new TSocket("localhost", FrameworkInformation.BOOTSTRAP_PORT);
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

    /**
     * Prepares the new job, selects a distributor nodes and sends the job
     *
     * @param taskList list of tasks
     */
    public void submitJob(Map<String, TaskData> taskList) {
        int taskCounter = 0;
        for (TaskData task : taskList.values()) {
            int taskId = Math.abs((jobIdString + "::" + taskCounter++).hashCode());
            addTask(taskId, task.getClassFile(), task.getInputData(), task.getRequiredResources());
        }
        NodeInfo selectedNode = getDistributorNode();
        if (selectedNode != null) {
            sendJob(selectedNode);
            setJobHandlerNode(selectedNode);
        } else {
            log.warn("Could not get a distributor node");
        }
    }

    /**
     * Selects a volunteer node that will act as the job distributor
     *
     * @return Node information of the selected node
     */
    private NodeInfo getDistributorNode() {

        NodeInfo selectedMember = null;
        TTransport transport = new TSocket("localhost", FrameworkInformation.BOOTSTRAP_PORT);
        try {
            log.info("Getting list of members from bootstrap");
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            Siyapath.Client client = new Siyapath.Client(protocol);
            context.updateMemberSet(CommonUtils.deSerialize(client.getMembers()));
            log.info("Number of members from bootstrapper: " + context.getMemberCount());
            selectedMember = context.getRandomMember();
            setJobHandlerNode(selectedMember);
        } catch (TTransportException e) {
            if (e.getCause() instanceof ConnectException) {
                e.printStackTrace();
            }
        } catch (TException e) {
            e.printStackTrace();
        } finally {
            transport.close();
        }
        log.info("Selected node: " + selectedMember);

        return selectedMember;
    }

    /**
     * Sends the job to specified node
     *
     * @param node destination node
     */
    private void sendJob(NodeInfo node) {
        TTransport transport = new TSocket("localhost", node.getPort());
        try {
            log.info("Sending new job to node: " + node);
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            Siyapath.Client client = new Siyapath.Client(protocol);
            Job job = new Job(jobId, CommonUtils.serialize(context.getNodeInfo()), taskList);
            client.submitJob(job);
        } catch (TTransportException e) {
            if (e.getCause() instanceof ConnectException) {
                e.printStackTrace();
            }
        } catch (TException e) {
            e.printStackTrace();
        } finally {
            transport.close();
        }
    }

    /**
     * Creates and adds a new task to the job given a task class
     *
     * @param taskProgramFile class for the task to be created
     * @param inputData input data
     */
    private void addTask(int taskId, File taskProgramFile, String inputData ,
                         String requiredResources) {
        try {
            //TODO: implement assigning taskID, jobID. Client will ask JobScheduler/Handler for next available jobID
            Task task = new Task(taskId, jobId, CommonUtils.convertFileToByteBuffer
                    (taskProgramFile.getAbsolutePath()), inputData, getJobInterfaceName(),
                    CommonUtils.serialize(context.getNodeInfo()), null, requiredResources);

            taskList.put(taskId, task);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getJobInterfaceName() {
        return SiyapathTask.class.getName();
    }


    /**
     * @return the selected job handling node for the user
     */
    public NodeInfo getJobHandlerNode() {
        return jobHandlerNode;
    }

    /**
     * @param jobHandlerNode
     */
    public void setJobHandlerNode(NodeInfo jobHandlerNode) {
        this.jobHandlerNode = jobHandlerNode;
    }


    

    public void updateTableDataVectors(){

        if(!allRows.isEmpty()){
            allRows.removeAllElements();
        }
        for(Map.Entry<Integer,String> task : taskCompletionDataMap.entrySet()){
            eachRow = new Vector<String>();
            eachRow.add(task.getKey().toString());
            eachRow.add(task.getValue());
            allRows.add(eachRow);
        }
    }

    /**
     * Starts a new thread for polling status from JobProcessor
     */
    public void startPollingThread(int jobId, JTable table){
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
        
        JobStatusPollThread(int jobId, JTable table){
            this.jobId = jobId;
            this.table = table;
        }
        
        @Override
        public void run() {
            log.info("Polling thread started for JobId: " + jobId);
    
            jobStatus=false;
    
            while (!jobStatus) {
                count++;
                pollStatusFromJobProcessor(jobId);
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        /**
         * Contacts back the selected JobProcessor to get job status
         * @param jobID
         */
        public void pollStatusFromJobProcessor(int jobID){

            TTransport transport = new TSocket("localhost", getJobHandlerNode().getPort());

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
            } catch (Exception e){
                e.printStackTrace();
            }
            finally {
                transport.close();
            }
        }

        public boolean assessJobStatusFromTaskStatuses(Map<Integer, String> statusMap){

            boolean statusCondition = true;
            boolean eachTaskStatus;

            for(String taskStatus : statusMap.values()){
                if(taskStatus.equalsIgnoreCase("DONE")){
                    eachTaskStatus = true;
                }else{
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
