package org.siyapath.ui;

import org.siyapath.NodeContext;
import org.siyapath.NodeInfo;

/**
 * Created with IntelliJ IDEA.
 * User: Amila Manoj
 * Date: 8/7/12
 * Time: 2:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class NodeUIHandler {

    private NodeContext nodeContext;
    private NodeGUI ui;

    public NodeUIHandler(NodeContext context, NodeGUI ui) {
        this.ui = ui;
        this.nodeContext = context;
    }

    public void startMonitor() {
        new Thread(new UIUpdater()).start();
    }

    private class UIUpdater implements Runnable {
        private boolean active;
        @Override
        public void run() {
             active = true;

            while (active) {

                ui.setListenerStat(nodeContext.isListenerEnabled() ? "ON" : "OFF");
                ui.setWorkerStat(nodeContext.isWorkerEnabled() ? "ON" : "OFF");
                ui.setMemberCount(String.valueOf(nodeContext.getMemberCount()));
                StringBuilder members = new StringBuilder();

                for (NodeInfo node: nodeContext.getMemberSet()) {
                    members.append(node.getIp());
                    members.append("\n");
                }



                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
