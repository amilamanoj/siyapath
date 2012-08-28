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
import org.siyapath.SiyapathConstants;
import org.siyapath.service.*;
import org.siyapath.utils.CommonUtils;

import java.net.ConnectException;

/*
* Required implementation for the submit task operation on IDL, at the recipient's end.
* Assumes that a single .class is sent by the JobProcessor node ftm.
* TODO: extend to use a .zip/jar with multiple .class files
* */
public class TaskProcessor extends Thread {

    private final Log log = LogFactory.getLog(TaskProcessor.class);
    private Task task;
    private NodeContext context;
    private LivenessNotifier notifier;
    private SiyapathTask taskInstance;
    private SiyapathSecurityManager siyapathSecurityManager;
    private SecurityManager defaultSecurityManager;
    private Result taskResult;


    /**
     * @param task
     */
    public TaskProcessor(String name, Task task, NodeContext nodeContext) {
        super(name);
        this.task = task;
        context = nodeContext;
        context.increaseProTasksNo();
        notifier = new LivenessNotifier("LivenessNotifier-" + context.getNodeInfo().toString());
        siyapathSecurityManager = new SiyapathSecurityManager("secpass");
        defaultSecurityManager = System.getSecurityManager();
        taskResult = new Result(task.getJobID(), task.getTaskID(), null, CommonUtils.serialize(context.getNodeInfo()));
    }

    /*public void startProcessing(){
        context.increaseProTasksNo();
        this.start();
        context.decreaseProTasksNo();
    }*/


    public void run() {
        log.debug("Preparing to start the task: " + task.getTaskID());
        try {
            taskInstance = getTaskInstance();
            TaskThread taskThread = new TaskThread("task-thread id:" + task.getTaskID());
            notifier.start();

            // sand-boxing with a custom security manager that denies most permissions
//        System.setSecurityManager(siyapathSecurityManager);
            taskThread.start();
            try {
                taskThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            siyapathSecurityManager.disable("secpass");
//          System.setSecurityManager(defaultSecurityManager);
            log.info("Task processing is finished. ID: " + task.getTaskID());
            notifier.stopNotifier();
        } catch (Exception e) {
            log.error("Task program instantiation failed. Aborting task: " + task.getTaskID());
            taskResult.setResults("<aborted>".getBytes());
        }
        for (int i = 0; i < 3; i++) {
            if (deliverTaskResult(taskResult)) {
                break;
            }
            log.warn("Couldn't send result to distributor. task: " + task.getTaskID());
        }
        context.decreaseProTasksNo();
    }

    private class TaskThread extends Thread {

        private TaskThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            processTask();
        }
    }

    private void processTask() {
        try {
            log.info("Starting the task: " + task.getTaskID());
            taskInstance.setData(task.getTaskData());
            taskInstance.process();
            byte[] finalResult = taskInstance.getResults();
            taskResult.setResults(finalResult);
            log.debug("Task processing is successful. ID: " + task.getTaskID());

        } catch (SecurityException e) {
//            siyapathSecurityManager.disable("secpass");
//            System.setSecurityManager(defaultSecurityManager);
            log.error("Task Processing aborted due to an attempt of illegal operation");
            taskResult.setResults("<aborted>".getBytes());
        }
    }

    private SiyapathTask getTaskInstance() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        TaskClassLoader taskClassLoader;
        taskClassLoader = new TaskClassLoader();
        // TODO: verify if expected name is necessary
        Class theLoadedClass = null;
        theLoadedClass = taskClassLoader.loadClassToProcess(task.getTaskProgram(), null);

        Object object = theLoadedClass.newInstance();
        if (object instanceof SiyapathTask) {
            return (SiyapathTask) object;
        }
        return null;
    }


    private boolean deliverTaskResult(Result result) {

        boolean success = false;

        TTransport transport = new TSocket(task.getSender().getIp(), task.getSender().getPort());
        try {
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            Siyapath.Client client = new Siyapath.Client(protocol);
            log.info("Sending computed result back to Distributing node." + task.getSender());
            client.sendTaskResult(result);
            success = true;
        } catch (TTransportException e) {
            if (e.getCause() instanceof ConnectException) {
                log.warn("Task Distributor is no longer available on port: " + task.getSender());
            } else {
                log.warn(e.getMessage());
            }
        } catch (TException e) {
            e.printStackTrace();
        } finally {
            transport.close();
        }
        return success;
    }

//    private void sendResultToBackupNode(Result result) {
//        TTransport transport = new TSocket(task.getBackup().getIp(), task.getBackup().getPort());
//        try {
//            transport.open();
//            TProtocol protocol = new TBinaryProtocol(transport);
//            Siyapath.Client client = new Siyapath.Client(protocol);
//            log.info("Sending computed result to the backup node." + task.getBackup());
//            client.sendTaskResult(result);
//        } catch (TTransportException e) {
//            e.printStackTrace();
//        } catch (TException e) {
//            e.printStackTrace();
//        }
//    }

    private class LivenessNotifier extends Thread {
        private boolean isRunning = false;

        private LivenessNotifier(String name) {
            super(name);
        }

        @Override
        public void run() {
            isRunning = true;
            while (isRunning) {
                sendUpdate();
                try {
                    sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void sendUpdate() {
            TTransport transport = new TSocket(task.getSender().getIp(), task.getSender().getPort());
            try {
                transport.open();
                TProtocol protocol = new TBinaryProtocol(transport);
                Siyapath.Client client = new Siyapath.Client(protocol);
                client.notifyTaskLiveness(task.getTaskID());
                log.debug("Sending task update to Distributing node. TaskID: " + task.getTaskID());
            } catch (TTransportException e) {
                e.printStackTrace();
            } catch (TException e) {
                e.printStackTrace();
            } finally {
                transport.close();
            }
        }

        private void stopNotifier() {
            isRunning = false;
        }
    }

//    private class MonitorThread extends Thread {
//
//        public boolean isRunning = false;
//        LimitedCpuUsageMonitor monitor = new LimitedCpuUsageMonitor();
//
//        @Override
//        public void run() {
//
//            isRunning = true;
//            while (isRunning) {
//                System.out.println("Cpu usage: " + monitor.getCpuUsage());
//                try {
//                    sleep(2);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        public void stopMonitor() {
//            isRunning = false;
//        }
//    }
}
