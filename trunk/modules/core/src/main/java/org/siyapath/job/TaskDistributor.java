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

import java.io.*;
import java.net.ConnectException;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Random;


/*
* Assuming the task distributing node has the .class sent by user saved on disk by this time
* TODO: select a member from member list to assign the task to.
* */
public class TaskDistributor {
    
    private static final Log log = LogFactory.getLog(TaskDistributor.class);
    Task task = null;
//    private Map<Integer,Task> taskProcessingNodes = null;   //nodeID of processing node and task
    private Map<SiyapathNode, Task> taskProcessingNodes = null;
    private Map<Integer,Task> tasks = null;   //taskID and task

    TaskDistributor(Task task){
        tasks.put(task.taskID,task);
    }
    /*
    * main method is for demo purpose only, until user job submission is implemented.
    * */
    public static void main(String[] args) {
//        TaskDistributor taskDistributor = new TaskDistributor();
//        taskDistributor.send();
    }

    public Task createTask(){

        //temporary values have been set.
        try {
            task = new Task(123, 234, convertFileToByteBuffer(),"Sending a Temp task data in a String.","org.test.siyapath.CalcDemo");
            log.info("Created a task out of parameters submitted by user.");
        } catch (IOException e) {
            log.warn("Could not create a task out of given parameters.");
            e.printStackTrace();
        }
    return task;
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

//            if(taskProcessingNodes==null){
//                taskProcessingNodes = new HashMap<SiyapathNode,Task>();
//            }
//            taskProcessingNodes.put(new Integer(pickedProcessingNode),createTask());//

        } catch (TTransportException e) {
            e.printStackTrace();
        } catch (TException e) {
            e.printStackTrace();
        }

    return pickedProcessingNode;

    }

    public void send(){

        int temporaryRecipientPort = 9020;  //bootstrap port itself

        log.info("Attempting to connect to selected task-processing-node on port " + temporaryRecipientPort );
        TTransport transport = new TSocket("localhost",temporaryRecipientPort);

        try {
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            Siyapath.Client client = new Siyapath.Client(protocol);
            log.info("Submitting task to task-processing-node on port " + temporaryRecipientPort);
            client.submitTask(createTask());
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

    public ByteBuffer convertFileToByteBuffer() throws IOException {

        /*temporary location has been set*/
        final String BINARY_FILE_NAME = "C:\\Development\\CalcDemo.class";
        File file = new File(BINARY_FILE_NAME);
        InputStream inputStream = null;

        byte[] bytes = new byte[(int)file.length()];
        if (file.length() > Integer.MAX_VALUE) {
            log.error("File is too large.");
        }

        try{
//            bytes = new byte[(int)file.length()];  TODO: max file size?
//            if (file.length() > Integer.MAX_VALUE) {
//                log.error("File is too large.");
//            }
            inputStream = new BufferedInputStream(new FileInputStream(file));
            int offset=0, numRead;

            while (offset < bytes.length
                   && (numRead=inputStream.read(bytes, offset, bytes.length-offset)) >= 0) {
                offset += numRead;
            }
            // Ensure all the bytes have been read
            if (offset < bytes.length) {
                log.warn("Could not completely read file " + file.getName());
                throw new IOException("Could not completely read file " + file.getName());
            }else{
                log.info("Successfully located and read binary.");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return ByteBuffer.wrap(bytes);

    }
}
