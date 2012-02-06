package org.siyapath;

import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;

import java.util.concurrent.CountDownLatch;


/**
 * Created by IntelliJ IDEA.
 * User: amila
 * Date: 2/6/12
 * Time: 8:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class PeerListener {
    TServerTransport serverTransport;
    TServer server;
    private int port;
    private CountDownLatch cdLatch;


    public PeerListener(GossipService.Processor processor, int port) {
        this.port= port;
        initializeThriftServer(processor);
    }

    private void initializeThriftServer(GossipService.Processor processor) {
        try {
            serverTransport = new TServerSocket(port);
            server = new TSimpleServer(new TServer.Args(serverTransport).processor(processor));
            // Use this for a multithreaded server
            // TServer server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(processor));
        } catch (TTransportException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        System.out.println("Starting to listen for incoming connections");
        new ListenerThread().start();
        while (!isRunning()){
                //wait until server starts
        }
        System.out.println("Now listening for incoming connections on port " + port);
//        System.out.println(isRunning());

    }

    public boolean isRunning() {
        return server.isServing();
    }

    public void stop() {
        System.out.println("Stopping listening for incoming connections");
        server.stop();
    }

    private class ListenerThread extends Thread {

        @Override
        public void run() {


            server.serve();
//            server.stop();

        }
    }
}
