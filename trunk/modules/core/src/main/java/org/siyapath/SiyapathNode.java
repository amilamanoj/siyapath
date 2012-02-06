package org.siyapath;

/**
 * Date: 2/1/12
 * Time: 4:44 PM
 */

import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.net.ConnectException;
import java.util.Random;

public class SiyapathNode {

    private GossipServiceHandler handler;
    private GossipService.Processor processor;
    private PeerListener peerListener;
    private NodeContext nodeContext;
    private int nodePort;


    public SiyapathNode() {
        nodeContext = new NodeContext();
        handler = new GossipServiceHandler();
        processor = new GossipService.Processor(handler);
        nodePort = -1;

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
                this.nodePort = FrameworkInformation.BOOTSTRAP_PORT;
            } else {
                System.out.println("Bootstrapper is up and running");

                this.nodePort = new Random().nextInt(1000) + 9021;
            }

            peerListener = new PeerListener(processor, this.nodePort);

            peerListener.start();

        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    private boolean connectToBootStrapper() {
        boolean isBootStrapperAlive = false;
        System.out.println("Tring to connect to a bootstrapper node");
        TTransport transport = new TSocket("localhost", FrameworkInformation.BOOTSTRAP_PORT);
        try {
            transport.open();
            isBootStrapperAlive = true;
        } catch (TTransportException e) {
//            e.printStackTrace();
            if (e.getCause() instanceof ConnectException) {
                System.out.println("Could not connect to the bootstrapper :(");
            }
        } finally {
            transport.close();
        }

        return isBootStrapperAlive;

    }


}
