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
import org.siyapath.gossip.GossipImpl;
import org.siyapath.monitor.LimitedCpuUsageMonitor;
import org.siyapath.utils.CommonUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ConnectException;

/*
* Required implementation for the submit task operation on IDL, at the recipient's end.
* Assumes that a single .class is sent by the TaskDistributor node ftm.
* TODO: extend to use a .zip/jar with multiple .class files
* */
public class TaskProcessor {

    private final Log log = LogFactory.getLog(TaskProcessor.class);
    private Task task;
    private Class theLoadedClass;
    TaskClassLoader taskClassLoader;
    private String finalResult;
    int computedResultToBeHandedOverTo;
    private NodeContext context;

    /**
     * @param task
     */
    public TaskProcessor(Task task, NodeContext nodeContext) {
        this.task = task;
        computedResultToBeHandedOverTo = task.getSender().getPort();
        context = nodeContext;

//        to be used for jar
//        for(String name : names){}
    }

    public void processTask() {
        taskClassLoader = new TaskClassLoader();
        try {
            theLoadedClass = taskClassLoader.loadClassToProcess(task.getTaskProgram(), task.getClassName());
            Object instanceForTesting = theLoadedClass.newInstance();
            log.info("Task processing begins.");
            //currently uses the org.siyapath.sample.CalcDemo.processSampleJob() method
            Method method = theLoadedClass.getMethod("processSampleJob", null);
            MonitorThread monitor = new MonitorThread();
            monitor.start();
            finalResult = (String) method.invoke(instanceForTesting);
            monitor.stopMonitor();
            log.info("Task processing is completed.");
            task.setTaskResult(finalResult);
            sendResultToDistributingNode();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
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

    private class MonitorThread extends Thread {

        public boolean isRunning = false;
        LimitedCpuUsageMonitor monitor = new LimitedCpuUsageMonitor();

        @Override
        public void run() {

            isRunning = true;
            while (isRunning) {
                System.out.println("Cpu usage: " +monitor.getCpuUsage());
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
