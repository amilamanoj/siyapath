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
        NodeInfo thisInfo = new NodeInfo();
        SiyapathNode node = new SiyapathNode(thisInfo);
        node.startSiyapathNode();
    }

    public void startSiyapathNode() {
        log.info("Initializing Siyapath Node...");
        nodeContext.setNodeStatus(NodeContext.NodeStatus.STARTING);

        if (!connectToBootStrapper()) {
            log.info("OK, I'm gonna be the bootstrapper");
            nodeContext.setBootstrapper(true);
            nodeContext.getNodeInfo().setPort(FrameworkInformation.BOOTSTRAP_PORT);
        } else {
            log.info("Bootstrapper is up and running");
        }

        peerListener = new PeerListener(processor, nodeContext);
        peerListener.start();

        peerWorker = new PeerWorker(nodeContext);
        peerWorker.start();

        nodeContext.setNodeStatus(NodeContext.NodeStatus.LISTENING);
    }

    /**
     * @return true if a bootstrapper node exists, false otherwise
     */
    private boolean connectToBootStrapper() {
        boolean isBootStrapperAlive = false;
        log.info("Trying to connect to a bootstrapper node");
        TTransport transport = new TSocket("localhost", FrameworkInformation.BOOTSTRAP_PORT);
        try {
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            Siyapath.Client client = new Siyapath.Client(protocol);
            client.notifyPresence(CommonUtils.serialize(nodeContext.getNodeInfo()));
            isBootStrapperAlive = true;
        } catch (TTransportException e) {
//            e.printStackTrace();
            if (e.getCause() instanceof ConnectException) {
                log.warn("Could not connect to the bootstrapper :(");
            }
        } catch (TException e) {
            e.printStackTrace();
        } finally {
            transport.close();
        }
        return isBootStrapperAlive;
    }

    public NodeContext getNodeContext() {
        return nodeContext;
    }

}
