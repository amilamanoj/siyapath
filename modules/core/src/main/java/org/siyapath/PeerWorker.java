package org.siyapath;


/**
 * Created by IntelliJ IDEA.
 * User: amila
 * Date: 2/6/12
 * Time: 9:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class PeerWorker {

    private NodeContext nodeContext;

    public PeerWorker(NodeContext nodeContext) {
        this.nodeContext = nodeContext;
    }

    public void work() {
        if (!nodeContext.membersExist()) {
            initiateMembers();
        }
        new WorkerThread().start();
    }

    private void initiateMembers() {

    }

    private class WorkerThread extends Thread {

        public boolean isRunning = false;

        @Override
        public void run() {

            isRunning = true;

            while (isRunning) {

            }
        }
    }
}
