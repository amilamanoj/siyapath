package org.siyapath.job;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.siyapath.FrameworkInformation;
import org.siyapath.Siyapath;
import org.siyapath.SiyapathNode;
import org.siyapath.Task;

import java.net.ConnectException;
import java.util.Map;
import java.util.Random;


/*
* Assuming the task distributing node has the .class sent by user saved on disk by this time
* TODO: select a member from member list to assign the task to.
* */
public class TaskDistributor {
    
    private static final Log log = LogFactory.getLog(TaskDistributor.class);
    Task task = null;

    private Map<SiyapathNode, Task> taskProcessingNodes = null;

    private Map<Integer,Task> tasks = null;   //taskID and task

    public TaskDistributor(Task task){
        //Current implementation assumes only one task is on one node.
        this.task = task;
        //when one node handles multiple tasks, TBD
        tasks.put(task.taskID,task);
    }
    /*
    * main method is for demo purpose only, until user job submission is implemented.
    * */
    public static void main(String[] args) {
//        TaskDistributor taskDistributor = new TaskDistributor();
//        taskDistributor.send();
    }
    
    public int pickOneProcessingNode(){

        TTransport transport = new TSocket("localhost", FrameworkInformation.BOOTSTRAP_PORT);
        int pickedProcessingNode = 0;
        Object[]  memberArray = null;
        try{
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            Siyapath.Client client = new Siyapath.Client(protocol);
            /*
            * This is for the moment only, actually the task distributing node will maintain a set of member nodes as
            * every node does, and pick one out of them who has matching resources
            * */
            memberArray = client.getMembers().toArray();
            pickedProcessingNode = (Integer)memberArray[new Random().nextInt(memberArray.length)];

            log.info("Targeting processing node with node ID :" + pickedProcessingNode);

        } catch (TTransportException e) {
            e.printStackTrace();
        } catch (TException e) {
            e.printStackTrace();
        }

    return pickedProcessingNode;

    }

    public void send(){

        pickOneProcessingNode();
        
        int temporaryRecipientPort = 9020;  //bootstrap port itself on a so-thought node
//        task.setSender()
//        task.setRecipient(temporaryRecipientPort);
        log.info("Attempting to connect to selected task-processing-node on port " + temporaryRecipientPort );
        TTransport transport = new TSocket("localhost",temporaryRecipientPort);

        try {
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            Siyapath.Client client = new Siyapath.Client(protocol);
            log.info("Submitting task to task-processing-node on port " + temporaryRecipientPort);
            client.submitTask(task);
            log.info("Successfully submitted task to processing node!");
        } catch (TTransportException e) {
            e.printStackTrace();
            if(e.getCause() instanceof ConnectException){
                log.warn("No node is listening on port " + temporaryRecipientPort + ",assign task to another.");
            }
            //TODO: handle exception to pick other node
        } catch (TException e) {
            e.printStackTrace();
        }

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
