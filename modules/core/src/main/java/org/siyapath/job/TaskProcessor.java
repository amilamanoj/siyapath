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
import org.siyapath.monitor.LimitedCpuUsageMonitor;
import org.siyapath.service.NodeData;
import org.siyapath.service.Siyapath;
import org.siyapath.service.Task;
import org.siyapath.utils.CommonUtils;

import java.net.ConnectException;

/*
* Required implementation for the submit task operation on IDL, at the recipient's end.
* Assumes that a single .class is sent by the JobHandler node ftm.
* TODO: extend to use a .zip/jar with multiple .class files
* */
public class TaskProcessor {

    private final Log log = LogFactory.getLog(TaskProcessor.class);
    private Task task;
    int computedResultToBeHandedOverTo;
    private NodeContext context;
    private boolean taskStatus;

    /**
     * @param task
     */
    public TaskProcessor(Task task, NodeContext nodeContext) {
        this.task = task;
//        computedResultToBeHandedOverTo = task.getSender().getPort();
        context = nodeContext;
    }

    public void startProcessing() {
        log.info("Preparing to start the task: " + task.getTaskID());
        TaskThread jobThread = new TaskThread();
        jobThread.start();
    }

    private class TaskThread extends Thread {
        private Class theLoadedClass;
        private TaskClassLoader taskClassLoader;

        @Override
        public void run() {
            processTask();
        }

        private void processTask() {
            setTaskStatus(false);
            taskClassLoader = new TaskClassLoader();
            try {
                // TODO: verify if expected name is necessary
                theLoadedClass = taskClassLoader.loadClassToProcess(task.getTaskProgram(), null);
                SiyapathTask taskInstance = (SiyapathTask) theLoadedClass.newInstance();
//                MonitorThread monitor = new MonitorThread();
                taskInstance.setData(task.getTaskData());
                log.info("Starting the task: " + task.getTaskID() + " , Input: " + task.getTaskData());
                taskInstance.process();
//                monitor.start();
                String finalResult = (String) taskInstance.getResults();
//                monitor.stopMonitor();
                log.info("Task processing is completed.");
                log.info("Results: " + finalResult);
                task.setTaskResult(finalResult);
                setTaskStatus(true);
//            sendResultToDistributingNode();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }


    public void sendResultToDistributingNode() {


        NodeInfo nodeInfo = context.getNodeInfo();
        NodeData thisNode = CommonUtils.serialize(nodeInfo);
        //setting the new sender as the processing node
        task.setSender(thisNode);

        TTransport transport = new TSocket("localhost", computedResultToBeHandedOverTo);

        try {
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            Siyapath.Client client = new Siyapath.Client(protocol);
            log.info("Sending computed result back to Distributing node." + computedResultToBeHandedOverTo);
            client.sendTaskResult(task);

        } catch (TTransportException e) {
            e.printStackTrace();
            if (e.getCause() instanceof ConnectException) {
                log.warn("Task Distributor is no longer available on port: " + computedResultToBeHandedOverTo);
            }
        } catch (TException e) {
            e.printStackTrace();
        }
    }

    public boolean isTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(boolean taskStatus) {
        this.taskStatus = taskStatus;
    }

    private class MonitorThread extends Thread {

        public boolean isRunning = false;
        LimitedCpuUsageMonitor monitor = new LimitedCpuUsageMonitor();

        @Override
        public void run() {

            isRunning = true;
            while (isRunning) {
                System.out.println("Cpu usage: " + monitor.getCpuUsage());
                try {
                    sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void stopMonitor() {
            isRunning = false;
        }
    }
}
