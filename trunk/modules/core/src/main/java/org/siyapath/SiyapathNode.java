package org.siyapath;


import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.net.ConnectException;
import java.util.Random;

public class SiyapathNode {

    private GossipServiceHandler handler;
    private GossipService.Processor processor;
    private PeerListener peerListener;
    private PeerWorker peerWorker;
    private NodeContext nodeContext;
    private int nodePort;


    public SiyapathNode() {
        nodeContext = new NodeContext();
        handler = new GossipServiceHandler(nodeContext);
        processor = new GossipService.Processor(handler);
        nodePort = new Random().nextInt(1000) + 9021;

    }

    public static void main(String[] args) {

        SiyapathNode node = new SiyapathNode();
        node.startSiyapathNode();
    }

    private void startSiyapathNode() {
        try {
            System.out.println("Initializing Siyapath Node...");

            if (!connectToBootStrapper()) {
                System.out.println("OK, I'm gonna be the bootstrapper");
                nodeContext.setBootstrapper(true);
                this.nodePort = FrameworkInformation.BOOTSTRAP_PORT;
            } else {
                System.out.println("Bootstrapper is up and running");
            }

            peerListener = new PeerListener(processor, this.nodePort);
            peerListener.start();
            
            peerWorker = new PeerWorker(nodeContext);
            peerWorker.start();

        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    private boolean connectToBootStrapper() {
        boolean isBootStrapperAlive = false;
        System.out.println("Trying to connect to a bootstrapper node");
        TTransport transport = new TSocket("localhost", FrameworkInformation.BOOTSTRAP_PORT);
        try {
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            GossipService.Client client = new GossipService.Client(protocol);
            client.notifyPresence(nodePort);
            isBootStrapperAlive = true;
        } catch (TTransportException e) {
//            e.printStackTrace();
            if (e.getCause() instanceof ConnectException) {
                System.out.println("Could not connect to the bootstrapper :(");
            }
        } catch (TException e) {
            e.printStackTrace();
        } finally {
            transport.close();
        }

        return isBootStrapperAlive;

    }


}
