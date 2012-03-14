package org.siyapath;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;

import java.util.concurrent.CountDownLatch;


public class PeerListener {

    private static final Log log = LogFactory.getLog(PeerListener.class);

    private TServerTransport serverTransport;
    private TServer server;
    private int port;
    private CountDownLatch cdLatch;


    public PeerListener(Siyapath.Processor processor, int port) {
        initializeThriftServer(processor);
        this.port = port;
    }

    private void initializeThriftServer(Siyapath.Processor processor) {
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
        log.info("Starting to listen for incoming connections");
        new ListenerThread().start();
        while (!isRunning()) {
            log.debug("Waiting until the listener is started...");

        }
        log.info("Now listening for incoming connections on port " + port);

    }

    public boolean isRunning() {
        return server.isServing();
    }

    public void stop() {
        log.info("Stopping listening for incoming connections");
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
