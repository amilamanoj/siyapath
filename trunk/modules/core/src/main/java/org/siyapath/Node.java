package org.siyapath;

/**
 * Date: 2/1/12
 * Time: 4:44 PM
 */

import org.apache.thrift.TException;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.siyapath.utils.CommonUtils;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.util.ArrayList;

public class Node {

    public static GossipServiceHandler handler;

    public static GossipService.Processor processor;

    public static void main(String[] args) {
        try {

            handler = new GossipServiceHandler();
            processor = new GossipService.Processor(handler);

            Runnable listener = new Runnable() {
                public void run() {
                    listenerComponent(processor);
                }
            };
            Runnable worker = new Runnable() {
                public void run() {
                    workerComponent(processor);
                }
            };

            new Thread(listener).start();
//        new Thread(worker).start();

        } catch (Exception x) {
            x.printStackTrace();
        }
    }


    public static void listenerComponent(GossipService.Processor processor) {
        try {
            TServerTransport serverTransport = new TServerSocket(CommonUtils.getRandomAddress());
            TServer server = new TSimpleServer(new TServer.Args(serverTransport).processor(processor));

            // Use this for a multithreaded server
            // TServer server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(processor));

            System.out.println("Starting the listing for incoming connections...");
            server.serve();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
