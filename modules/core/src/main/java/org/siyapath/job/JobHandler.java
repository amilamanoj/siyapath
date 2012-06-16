package org.siyapath.job;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.siyapath.*;
import org.siyapath.service.*;
import org.siyapath.utils.CommonUtils;

import java.net.ConnectException;
import java.util.Map;


/*
* Assuming the task distributing node has the .class sent by user saved on disk by this time
* TODO: select a member from member list to assign the task to.
* */
public class JobHandler {

    private static final Log log = LogFactory.getLog(JobHandler.class);

    private Map<SiyapathNode, Task> taskProcessingNodes = null;
    private NodeContext context;
    private Map<Integer, Task> tasks = null;   //taskID and task

    public int getJobId() {
        return jobId;
    }

    private int jobId;

    public JobHandler(NodeContext nodeContext, int jobId, Map<Integer, Task> tasks) {
        this.jobId = jobId;
        context = nodeContext;
        this.tasks = tasks;
    }

    public void startJob() {
        log.info("Starting job:" + jobId);
        JobThread jobThread = new JobThread();
        jobThread.start();
    }

    private class JobThread extends Thread {

        @Override
        public void run() {

            JobScheduler scheduler = new DefaultJobScheduler(context);

            for (Task t : tasks.values()) {
                NodeInfo selectedNode = scheduler.selectTaskProcessorNode(t);
                log.info("JobID:" + jobId + "-TaskID:" + t.getTaskID() + "-Sending to: " + selectedNode);

                sendTask(t, selectedNode);
            }
        }
    }

    /**
     * Submits a task to a specified node
     * @param task task to submit
     * @param destinationNode node to submit to
     */
    public void sendTask(Task task, NodeInfo destinationNode) {            //changed

        NodeInfo nodeInfo = context.getNodeInfo();
        NodeData thisNode = CommonUtils.serialize(nodeInfo);
        task.setSender(thisNode);

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
