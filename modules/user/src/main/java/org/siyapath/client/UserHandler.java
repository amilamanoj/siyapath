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
import org.siyapath.job.SiyapathTask;
import org.siyapath.service.Siyapath;
import org.siyapath.service.Task;
import org.siyapath.utils.CommonUtils;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.util.HashMap;
import java.util.Map;

public class UserHandler {
    private static final Log log = LogFactory.getLog(UserHandler.class);

    private NodeContext context;
    private NodeInfo jobHandlerNode;
    private NodeInfo clientEnd;
    private Map taskList = new HashMap<Integer, Task>();
    private int jobId;
    private int taskCounter;

    public UserHandler() {
        this.clientEnd = new NodeInfo();
        this.context = new NodeContext(clientEnd);
        jobId = CommonUtils.getRandomNumber(1000);
    }

    public int getJobId() {
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
                e.printStackTrace();
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
        for (TaskData task : taskList.values()) {
            addTask(task.getClassFile(), task.getInputData());
        }
        NodeInfo selectedNode = getDistributorNode();
        if (selectedNode != null) {
            sendJob(selectedNode);
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
//        String res = null;
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
//                res = "connecEx";
                e.printStackTrace();
            }
        } catch (TException e) {
//            res = "tEx";
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
            client.submitJob(jobId, CommonUtils.serialize(context.getNodeInfo()), taskList);
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
    private void addTask(File taskProgramFile, String inputData) {
        try {
            int taskId = taskCounter++;
            //TODO: implement assigning taskID, jobID. Client will ask JobScheduler/Handler for next available jobID
            Task task = new Task(taskId, jobId, CommonUtils.convertFileToByteBuffer(taskProgramFile.getAbsolutePath()),
                    inputData, getJobInterfaceName(),
                    CommonUtils.serialize(context.getNodeInfo()), null);

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


    /**
     * Contacts back the selected JobHandler to get job status
     *
     * @param jobID
     */
    public void pollStatusOfJob(int jobID){
        //
        TTransport transport = new TSocket("localhost", getJobHandlerNode().getPort());
        boolean jobStatus = false;
        try {
            log.debug("Polling status of job: " + jobID);
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            Siyapath.Client client = new Siyapath.Client(protocol);
            jobStatus = client.getJobStatusFromJobHandler(jobID,getJobHandlerNode().getPort());
            log.debug("Status of " + jobID + " is " + jobStatus);
            //TODO: convey client, repeat at task level & content tbd after scheduler/handler has job id assignment
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

    private class JobStatusPollThread extends Thread {

        public boolean isRunning = false;

        @Override
        public void run() {

            isRunning = true;
            while (isRunning) {
                pollStatusOfJob(jobId);
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
    }

}
