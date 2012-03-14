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

import java.net.ConnectException;
import java.util.Random;

public class SiyapathNode {

    private static final Log log = LogFactory.getLog(SiyapathNode.class);

    private SiyapathService handler;
    private Siyapath.Processor processor;
    private PeerListener peerListener;
    private PeerWorker peerWorker;
    private NodeContext nodeContext;


    public SiyapathNode() {
        nodeContext = NodeContext.getInstance();
        handler = new SiyapathService();
        processor = new Siyapath.Processor(handler);
        nodeContext.setNodeID(CommonUtils.getRandomNumber(100000));

    }

    public static void main(String[] args) {

        SiyapathNode node = new SiyapathNode();
        node.startSiyapathNode();
    }

    private void startSiyapathNode() {
        try {
            log.info("Initializing Siyapath Node...");

            if (!connectToBootStrapper()) {
                log.info("OK, I'm gonna be the bootstrapper");
                nodeContext.setBootstrapper(true);
                nodeContext.getNodeInfo().setPort(FrameworkInformation.BOOTSTRAP_PORT);
            } else {
                log.info("Bootstrapper is up and running");
            }

            peerListener = new PeerListener(processor);
            peerListener.start();

            peerWorker = new PeerWorker();
            peerWorker.start();

        } catch (Exception x) {
            x.printStackTrace();
        }
    }

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


}
