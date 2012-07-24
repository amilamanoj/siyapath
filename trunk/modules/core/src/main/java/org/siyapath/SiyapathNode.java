package org.siyapath;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.siyapath.utils.CommonUtils;
import org.siyapath.service.*;

import java.net.ConnectException;

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

        if (args.length == 1) {
            if (args[0].equals("bs")) {
                nodeInfo.setBootstrapper(true);
                nodeInfo.setPort(FrameworkInformation.BOOTSTRAP_PORT);
            }
        }
        SiyapathNode node = new SiyapathNode(nodeInfo);
        node.startSiyapathNode();
    }

    public void startSiyapathNode() {
        nodeContext.setNodeStatus(NodeContext.NodeStatus.STARTING);
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
        peerWorker.start();

        nodeContext.setNodeStatus(NodeContext.NodeStatus.LISTENING);
    }



    public NodeContext getNodeContext() {
        return nodeContext;
    }

}
