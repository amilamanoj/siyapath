package org.siyapath.task;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.siyapath.NodeContext;
import org.siyapath.monitor.LimitedCpuUsageMonitor;
import org.siyapath.service.NodeStatus;
import org.siyapath.service.Result;
import org.siyapath.service.Siyapath;
import org.siyapath.service.Task;

import java.net.ConnectException;

/*
* Required implementation for the submit task operation on IDL, at the recipient's end.
* Assumes that a single .class is sent by the JobProcessor node ftm.
* TODO: extend to use a .zip/jar with multiple .class files
* */
public class TaskProcessor {

    private final Log log = LogFactory.getLog(TaskProcessor.class);
    private Task task;
    private NodeContext context;
    private boolean taskStatus;

    /**
     * @param task
     */
    public TaskProcessor(Task task, NodeContext nodeContext) {
        this.task = task;
        context = nodeContext;
    }

    public void startProcessing() {
        log.info("Preparing to start the task: " + task.getTaskID());
        TaskThread taskThread = new TaskThread();
        taskThread.start();
    }

    private class TaskThread extends Thread {

        @Override
        public void run() {
            processTask();
        }

    }

    private void processTask() {
        Result taskResult = new Result(task.getJobID(), task.getTaskID(), null);
        setTaskStatus(false);

        SiyapathTask taskInstance = getTaskInstance();

        if (taskInstance != null) {
            // MonitorThread monitor = new MonitorThread();
            SiyapathSecurityManager siyapathSecurityManager = new SiyapathSecurityManager("secpass");

            // sand-boxing with a custom security manager that denies most permissions
            SecurityManager oldSecurityManager = System.getSecurityManager();
            System.setSecurityManager(siyapathSecurityManager);
            try {
            taskInstance.setData(task.getTaskData());
            log.info("Starting the task: " + task.getTaskID() + " , Input: " + task.getTaskData());
            taskInstance.process();
//                monitor.start();
            String finalResult = (String) taskInstance.getResults();
//                monitor.stopMonitor();
            log.info("Task processing is completed.");

            siyapathSecurityManager.disable("secpass");
            System.setSecurityManager(oldSecurityManager);

            log.info("Results: " + finalResult.substring(0, 100));
            taskResult.setResults(finalResult);
            deliverTaskResult(taskResult);
            setTaskStatus(true);
            }
            catch (SecurityException e){
                // TODO: handle illegal operation
                siyapathSecurityManager.disable("secpass");
                System.setSecurityManager(oldSecurityManager);
                log.info("Task Processing aborted due to an attempt of illegal operation");
            }
        } else {
            // processing failed
        }
        context.getNodeInfo().setNodeStatus(NodeStatus.IDLE);
    }

    private SiyapathTask getTaskInstance() {
        TaskClassLoader taskClassLoader;
        taskClassLoader = new TaskClassLoader();
        // TODO: verify if expected name is necessary
        context.getNodeInfo().setNodeStatus(NodeStatus.BUSY);
        Class theLoadedClass = null;
        try {
            theLoadedClass = taskClassLoader.loadClassToProcess(task.getTaskProgram(), null);

            Object object = theLoadedClass.newInstance();

            if (object instanceof SiyapathTask) {
                return (SiyapathTask) object;
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }


    private void deliverTaskResult(Result result) {

        TTransport transport = new TSocket("localhost", task.getSender().getPort());

        try {
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            Siyapath.Client client = new Siyapath.Client(protocol);
            if (client.isAlive()) {
                log.info("Sending computed result back to Distributing node." + task.getSender());
                client.sendTaskResult(result);
            } else {
                log.warn("Task Distributor is no longer available on port: " + task.getSender());
                sendResultToBackupNode(result);
            }

        } catch (TTransportException e) {
            e.printStackTrace();
            if (e.getCause() instanceof ConnectException) {
                log.warn("Task Distributor is no longer available on port: " + task.getSender());
            }
        } catch (TException e) {
            e.printStackTrace();
        } finally {
            transport.close();
        }
    }

    private void sendResultToBackupNode(Result result) {
        TTransport transport = new TSocket("localhost", task.getBackup().getPort());
        try {
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            Siyapath.Client client = new Siyapath.Client(protocol);
            log.info("Sending computed result to the backup node." + task.getBackup());
            client.sendTaskResult(result);
        } catch (TTransportException e) {
            e.printStackTrace();
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
