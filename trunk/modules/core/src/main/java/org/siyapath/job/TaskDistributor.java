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
import org.siyapath.utils.CommonUtils;

import java.net.ConnectException;
import java.util.Map;


/*
* Assuming the task distributing node has the .class sent by user saved on disk by this time
* TODO: select a member from member list to assign the task to.
* */
public class TaskDistributor {
    
    private static final Log log = LogFactory.getLog(TaskDistributor.class);
    Task task = null;
    int computedResultToBeHandedOverTo;
    int myTaskProcessingNodePort;

    private Map<SiyapathNode, Task> taskProcessingNodes = null;
    private NodeContext context;
    private Map<Integer,Task> tasks = null;   //taskID and task

    /**
     *
     * @param task
     */
    public TaskDistributor(Task task ){
        context=NodeContext.getInstance();
        //Current implementation assumes only one task is on one node.
        this.task = task;
        this.computedResultToBeHandedOverTo = task.getSender().getPort();
        //when one node handles multiple tasks, TBD
        //tasks.put(task.taskID,task);
    }
    /*
    * main method is for demo purpose only, until user job submission is implemented.
    * */
    public static void main(String[] args) {
//        TaskDistributor taskDistributor = new TaskDistributor();
//        taskDistributor.send();
    }


    
    public void sendTaskToProcessingNode(){
        NodeInfo selectedProcessingNode = getProcessingNode();
        myTaskProcessingNodePort = selectedProcessingNode.getPort();
        send(selectedProcessingNode);
    }

    public void send(NodeInfo processingNode){            //changed

//          int temporaryRecipientPort = 9020;  //bootstrap port itself on a so-thought node
////        task.setSender()
////        task.setRecipient(temporaryRecipientPort);

        NodeInfo nodeInfo = NodeContext.getInstance().getNodeInfo();
        NodeData thisNode = CommonUtils.serialize(nodeInfo);
        task.setSender(thisNode);
        
        log.info("Attempting to connect to selected task-processing-node on port " + myTaskProcessingNodePort );
        TTransport transport = new TSocket("localhost",myTaskProcessingNodePort);

        try {
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            Siyapath.Client client = new Siyapath.Client(protocol);
            log.info("Submitting task to task-processing-node on port " + myTaskProcessingNodePort);
            client.submitTask(task);
            log.info("Successfully submitted task to processing node!");
        } catch (TTransportException e) {
            e.printStackTrace();
            if(e.getCause() instanceof ConnectException){
                log.warn("No node is listening on port " + myTaskProcessingNodePort + ",assign task to another.");
            }
            //TODO: handle exception to pick other node
        } catch (TException e) {
            e.printStackTrace();
        }

    }

    private NodeInfo getProcessingNode() {
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

    public void sendResultToUserNode(){

        TTransport transport = new TSocket("localhost",task.getSender().getPort());

        try {
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            Siyapath.Client client = new Siyapath.Client(protocol);
            log.info("Sending computed result back to User node." + task.getSender());
            client.sendTaskResult(task);

        } catch (TTransportException e) {
            e.printStackTrace();
            if(e.getCause() instanceof ConnectException){
                log.warn("User is no longer available on port: " + task.getSender());
            }
        } catch (TException e) {
            e.printStackTrace();
        }
    }
}
