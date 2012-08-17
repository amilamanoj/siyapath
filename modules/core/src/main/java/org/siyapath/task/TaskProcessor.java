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
    private boolean finished;
    private Result taskResult;


    /**
     * @param task
     */
    public TaskProcessor(String name, Task task, NodeContext nodeContext) {
        super(name);
        this.task = task;
        this.finished = false;
        context = nodeContext;
        notifier = new LivenessNotifier("LivenessNotifier-" + context.getNodeInfo().toString());
        siyapathSecurityManager = new SiyapathSecurityManager("secpass");
        defaultSecurityManager = System.getSecurityManager();
        taskResult = new Result(task.getJobID(), task.getTaskID(), null, CommonUtils.serialize(context.getNodeInfo()));

    }


    public void run() {
        if (context.getNodeInfo().getNodeStatus() == NodeStatus.IDLE) {  //TODO: need to reject tasks if not idle
            context.getNodeInfo().setNodeStatus(NodeStatus.PROCESSING_IDLE);
        }
        log.info("Preparing to start the task: " + task.getTaskID());
        TaskThread taskThread = new TaskThread("task-thread id:" + task.getTaskID());
        taskInstance = getTaskInstance();
        notifier.start();

        // sand-boxing with a custom security manager that denies most permissions
        System.setSecurityManager(siyapathSecurityManager);

        log.info("Starting the task: " + task.getTaskID() + " , Input: " + task.getTaskData());
        taskThread.start();

        while (!finished) {
            try {
                Thread.sleep(1000);  // wait until task is finished
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        siyapathSecurityManager.disable("secpass");
        System.setSecurityManager(defaultSecurityManager);
        log.info("Task processing is completed. ID: " + task.getTaskID());
//        log.debug("Results: " + taskResult.getResults().substring(0, 8) + "...");
        notifier.stopNotifier();

        deliverTaskResult(taskResult);

    }

    private void setTaskFinished() {
        this.finished = true;
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
            taskInstance.setData(task.getTaskData());
            taskInstance.process();
//                monitor.start();
            byte[] finalResult = taskInstance.getResults();
//                monitor.stopMonitor();
            taskResult.setResults(finalResult);

        } catch (SecurityException e) {
            // TODO: handle illegal operation
//            siyapathSecurityManager.disable("secpass");
//            System.setSecurityManager(defaultSecurityManager);
            log.warn("Task Processing aborted due to an attempt of illegal operation");
            taskResult.setResults("<Aborted>".getBytes());

        } finally {
            setTaskFinished();
            if (context.getNodeInfo().getNodeStatus() != NodeStatus.DISTRIBUTING) {  //TODO: need to reject accepting tasks if not idle
                context.getNodeInfo().setNodeStatus(NodeStatus.IDLE);
            }
        }
    }

    private SiyapathTask getTaskInstance() {
        TaskClassLoader taskClassLoader;
        taskClassLoader = new TaskClassLoader();
        // TODO: verify if expected name is necessary

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

        TTransport transport = new TSocket(task.getSender().getIp(), task.getSender().getPort());

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
        TTransport transport = new TSocket(task.getBackup().getIp(), task.getBackup().getPort());
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
