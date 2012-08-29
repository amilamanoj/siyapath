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
import org.siyapath.service.Job;
import org.siyapath.service.Result;
import org.siyapath.service.Siyapath;

import java.net.ConnectException;
import java.util.HashMap;
import java.util.Map;

public class BackupHandler {

    private static final Log log = LogFactory.getLog(BackupHandler.class);

    private NodeContext context;
    private Job jobToBackup;
    private Map<Integer, ProcessingTask> taskMap;
    private NodeInfo nodeToBackup;
    private StatusThread statusThread;

    public BackupHandler(NodeContext nodeContext) {
        this.context = nodeContext;
    }

    /**
     * @param job          Job
     * @param nodeToBackup Node that has to be backed-up
     * @return true if request is accepted, false otherwise
     */
    public boolean requestBecomeBackup(Job job, NodeInfo nodeToBackup) {
        if (context.isBackup() || !context.getNodeResource().isIdle()) {
            return false;
        }
        context.setBackup(true);
        this.jobToBackup = job;
        this.nodeToBackup = nodeToBackup;
        this.taskMap = new HashMap<Integer, ProcessingTask>();
        statusThread = new StatusThread();
        statusThread.start();

        return true;
    }

    public void updateTaskResult(Result result) {                   //changed
//        ProcessingTask task = new ProcessingTask(result.getJobID(), result.getTaskID(),
//                TaskStatus.DONE);
//        task.setResult(result.getResults());
       // task.setProcessingNode(CommonUtils.deSerialize(result.getProcessingNode()));
//        taskMap.put(result.getTaskID(), task);
    }

    private class StatusThread extends Thread {
        private boolean isRunning = true;

        @Override
        public void run() {
            while (isRunning) {
                boolean isAlive = isNodeAlive();
                if (!isAlive) {
                    log.warn("Job Distributor is no longer available: " + nodeToBackup);
                    context.getJobProcessor().addTasksToTaskMap(taskMap);
                    context.getJobProcessor().addNewJob(jobToBackup);
                    endBackup();
                }
                try {
                    Thread.sleep(20000);
                } catch (InterruptedException e) {
                }
            }
        }

        public void stopThread() {
            isRunning = false;
        }

        private boolean isNodeAlive() {
            TTransport transport = new TSocket(nodeToBackup.getIp(), nodeToBackup.getPort());
            try {
                transport.open();
                TProtocol protocol = new TBinaryProtocol(transport);
                Siyapath.Client client = new Siyapath.Client(protocol);
                return client.isAlive();
            } catch (TException e) {
                log.error("Error contacting job processor to check liveness" + e.getMessage());
            } finally {
                transport.close();
            }
            return false;
        }
    }

    public void endBackup() {
        statusThread.stopThread();
        taskMap = null;
        jobToBackup = null;
        nodeToBackup = null;
        context.setBackup(false);
    }


}
