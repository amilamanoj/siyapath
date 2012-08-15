package org.siyapath;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;
import org.siyapath.service.Siyapath;

import java.util.concurrent.CountDownLatch;


public class PeerListener {

    private static final Log log = LogFactory.getLog(PeerListener.class);

    private TServerTransport serverTransport;
    private TServer server;
    private CountDownLatch cdLatch;
    private NodeContext nodeContext;


    /**
     * @param processor
     * @param nodeContext
     */
    public PeerListener(Siyapath.Processor processor, NodeContext nodeContext) {
        this.nodeContext = nodeContext;
        initializeThriftServer(processor);
    }

    /**
     * @param processor
     */
    private void initializeThriftServer(Siyapath.Processor processor) {
        try {
            log.debug("Initializing thrift server with the port:" + nodeContext.getNodeInfo().getPort());
            serverTransport = new TServerSocket(nodeContext.getNodeInfo().getPort());
            //simple server
            // server = new TSimpleServer(new TServer.Args(serverTransport).processor(processor));
            // Thread Pool Server
            server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(processor));
            //Threaded Selector Server
            //TNonblockingServerTransport serverTransport1 = new TNonblockingServerSocket(nodeContext.getNodeInfo().getPort());
            // server = new TThreadedSelectorServer(new TThreadedSelectorServer.Args(serverTransport1).processor(processor));
            //Half Sync/Half Async  Server
            // server = new THsHaServer(new THsHaServer.Args(serverTransport1).processor(processor));
            //Non blocking Server
            // server = new TNonblockingServer(new TNonblockingServer.Args(serverTransport1).processor(processor));

        } catch (TTransportException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        log.debug("Starting to listen for incoming connections");
        new ListenerThread("ListenerThread-" + nodeContext.getNodeInfo().toString()).start();
        while (!isRunning()) {
            log.debug("Waiting until the listener is started...");

        }
        log.info("Now listening for incoming connections on port " + nodeContext.getNodeInfo().getPort());

    }

    /**
     * @return true if thrift server thread is serving, false otherwise
     */
    public boolean isRunning() {
        return server.isServing();
    }

    public void stop() {
        log.info("Stopping listening for incoming connections");
        server.stop();
        nodeContext.setListenerEnabled(false);
    }

    private class ListenerThread extends Thread {

        private ListenerThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            nodeContext.setListenerEnabled(true);
            try {
                server.serve();
            } catch (SecurityException e) {
                log.error("Could not gossip due to Security Exception");
            }

        }
    }
}
