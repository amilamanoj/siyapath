/*
 * Distributed under the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

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
import org.siyapath.service.Result;
import org.siyapath.service.Siyapath;
import org.siyapath.service.Task;
import org.siyapath.service.TaskStatus;
import org.siyapath.utils.CommonUtils;

import java.net.ConnectException;

/**
 * Instantiates
 * Required implementation for the submit task operation on IDL, at the recipient's end.
 * Assumes that a single .class is sent by the JobProcessor node ftm.
 */
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
        context.increaseProTasksNo(task.getTaskID());
        notifier = new LivenessNotifier("LivenessNotifier-" + context.getNodeInfo().toString());
        siyapathSecurityManager = new SiyapathSecurityManager("secpass");
        defaultSecurityManager = System.getSecurityManager();
        taskResult = new Result(task.getJobID(), task.getTaskID(), TaskStatus.PROCESSING, null,
                CommonUtils.serialize(context.getNodeInfo()), task.getTaskReplicaIndex());
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
            System.setSecurityManager(siyapathSecurityManager);
            siyapathSecurityManager.disable("secpass");
            taskThread.start();
            try {
                taskThread.join();
            } catch (InterruptedException e) {
                log.warn("Thread was interrupted while waiting for task thread to complete. " + e.getMessage());
            }
            siyapathSecurityManager.disable("secpass");
            System.setSecurityManager(defaultSecurityManager);
            log.info("Task processing is finished. ID: " + task.getTaskID());
            notifier.stopNotifier();
        } catch (Exception e) {
            log.error("Task program instantiation failed. Aborting task: " + task.getTaskID());
            taskResult.setStatus(TaskStatus.ABORTED_ERROR);
            taskResult.setResults("<aborted_error>".getBytes());
        }
        for (int i = 0; i < 3; i++) {
            if (deliverTaskResult(taskResult)) {
                break;
            }
            log.warn("Couldn't send result to distributor. task: " + task.getTaskID());
        }
        context.decreaseProTasksNo(task.getTaskID());
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

    /**
     * Process the task using instantiated java class generated through reflection
     */
    private void processTask() {
        try {
            log.info("Starting the task: " + task.getTaskID());
            taskInstance.setData(task.getTaskData());
            taskInstance.process();
//            taskInstance.setMetaData(String.valueOf(context.getNodeResource().getNodeInfo().getNodeId()));
            byte[] finalResult = taskInstance.getResults();
            taskResult.setResults(finalResult);
            taskResult.setStatus(TaskStatus.COMPLETED);
            log.debug("Task processing is successful. ID: " + task.getTaskID());
        } catch (SecurityException e) {
            siyapathSecurityManager.disable("secpass");
            log.error("Task Processing aborted due to an attempt of illegal operation: " + e.getMessage());
            taskResult.setStatus(TaskStatus.ABORTED_SECURITY);
            taskResult.setResults("<aborted_security_error>".getBytes());
        } catch (ExceptionInInitializerError e) {
            siyapathSecurityManager.disable("secpass");
            if (e.getCause() instanceof SecurityException) {
                log.error("Task Processing aborted due to an attempt of illegal operation: " + e.getMessage());
                taskResult.setStatus(TaskStatus.ABORTED_SECURITY);
                taskResult.setResults("<aborted_security_error>".getBytes());
            }
            log.error("Task Processing aborted due to an error: " + e.getMessage());
            taskResult.setStatus(TaskStatus.ABORTED_SECURITY);
            taskResult.setResults("<aborted_error>".getBytes());
        } catch (Exception e) {
            siyapathSecurityManager.disable("secpass");
            log.error("Task Processing aborted due to an error: " + e.getMessage());
            taskResult.setStatus(TaskStatus.ABORTED_SECURITY);
            taskResult.setResults("<aborted_error>".getBytes());
        }
    }

    /**
     * @return Instance of java class implementing SiyapathTask interface, generated through reflection
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private SiyapathTask getTaskInstance() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        TaskClassLoader taskClassLoader;
        taskClassLoader = new TaskClassLoader();
        Class theLoadedClass = null;
        theLoadedClass = taskClassLoader.loadClassToProcess(task.getTaskProgram(), null);

        Object object = theLoadedClass.newInstance();
        if (object instanceof SiyapathTask) {
            return (SiyapathTask) object;
        }
        return null;
    }

    /**
     * @param result
     * @return true if result delivered to job processing node, false otherwise
     */
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
                    sleep(SiyapathConstants.TASK_TRACKER_INTERVAL);
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
                client.notifyTaskLiveness(task.getTaskID(), task.getTaskReplicaIndex());
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
