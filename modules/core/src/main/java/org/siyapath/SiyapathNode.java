package org.siyapath;

/**
 * Date: 2/1/12
 * Time: 4:44 PM
 */

import org.apache.thrift.TException;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Random;

public class SiyapathNode {

    private GossipServiceHandler handler;
    private GossipService.Processor processor;
    private PeerListener peerListener;
    private int nodePort;


    public SiyapathNode() {
        handler = new GossipServiceHandler();
        processor = new GossipService.Processor(handler);
        nodePort=-1;

    }

    public static void main(String[] args) {

        SiyapathNode node = new SiyapathNode();
        node.startSiyapathNode();
    }

    private void startSiyapathNode() {
        try {
            System.out.println("Initializing Siyapath Node...");

            if (!connectToBootStrapper()){
                System.out.println("Ok, I'm gonna be the bootstrapper");
                this.nodePort = 9020;
            }  else {
                this.nodePort = new Random().nextInt(1000)+9021;
            }

            peerListener = new PeerListener(processor,this.nodePort);

            peerListener.start();

        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    private boolean connectToBootStrapper() {
        boolean isBootStrapperAlive =false;
        System.out.println("Tring to connect to a bootstrapper node");
        TTransport transport = new TSocket("localhost", 9120);
        try {
            transport.open();
            isBootStrapperAlive = true;
        } catch (TTransportException e) {
//            e.printStackTrace();
            if (e.getCause() instanceof ConnectException){
                System.out.println("Could not connect to the bootstrapper :(");
            }
        } finally {
            transport.close();
        }
        
        return  isBootStrapperAlive;
            
    }


    public static void workerComponent(GossipService.Processor processor) {
        TTransport transport;
        try {
            System.out.println("Trying to find a bootstrapper...");
            ArrayList<InetSocketAddress> list = FrameworkInformation.getKnownBootstrappers();
            InetSocketAddress bNode = list.get(0);
            transport = new TSocket(bNode.getHostName(), bNode.getPort());
            transport.open();
        } catch (TException x) {
            x.printStackTrace();
        }
    }


}
