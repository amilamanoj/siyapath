package org.siyapath;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.siyapath.service.*;
import org.siyapath.service.NodeStatus;
import org.siyapath.ui.NodeGUI;
import org.siyapath.ui.NodeUIHandler;
import org.siyapath.utils.CommonUtils;

import javax.swing.*;

public class SiyapathNode {

    private static final Log log = LogFactory.getLog(SiyapathNode.class);

    private SiyapathService handler;
    private Siyapath.Processor processor;
    private PeerListener peerListener;
    private PeerWorker peerWorker;
    private NodeContext nodeContext;


    public SiyapathNode(NodeInfo nodeInfo) {
        nodeContext = new NodeContext(nodeInfo);
        handler = new SiyapathService(nodeContext);
        processor = new Siyapath.Processor(handler);
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        NodeInfo nodeInfo = new NodeInfo();
        boolean showUI = true;
        if (args.length == 1) {
            if (args[0].equals("bs")) {
                nodeInfo.setBootstrapper(true);
                nodeInfo.setPort(CommonUtils.getBootstrapperPort());
                showUI = false;
            } else if (args[0].equals("cl")) {
                showUI = false;
            }
        }
        SiyapathNode node = new SiyapathNode(nodeInfo);

        if (showUI) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (UnsupportedLookAndFeelException e) {
            } catch (ClassNotFoundException e) {
            } catch (InstantiationException e) {
            } catch (IllegalAccessException e) {
            }
            node.getNodeContext().setGuiEnabled(true);
            NodeGUI gui = new NodeGUI();
            gui.setVisible(true);
            NodeUIHandler uiHandler = new NodeUIHandler(node.getNodeContext(), gui);
            uiHandler.startMonitor();
        }
        node.startSiyapathNode();
    }

    public void startSiyapathNode() {
        nodeContext.getNodeResource().setNodeStatus(NodeStatus.STARTING);
        log.info("Initializing Siyapath Node: " + nodeContext.getNodeInfo());

        if (nodeContext.isBootstrapper()) {
            log.info("Starting Bootstrapper Node...");

        }
//      else if (!connectToBootStrapper()) {
//            log.info("OK, I'm gonna be the bootstrapper");
//            nodeContext.getNodeInfo().setPort(FrameworkInformation.BOOTSTRAP_PORT);
//        }

        peerListener = new PeerListener(processor, nodeContext);
        peerListener.start();
        peerWorker = new PeerWorker(nodeContext);
        nodeContext.getNodeResource().setNodeStatus(NodeStatus.IDLE);
        peerWorker.start();
    }


    public NodeContext getNodeContext() {
        return nodeContext;
    }

}
