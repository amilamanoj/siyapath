/*
 * Distributed under the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.siyapath;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.thrift.TException;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;
import org.siyapath.service.Siyapath;

/**
 * Listener component of the peer. Listens for incoming connections.
 */
public class PeerListener {

    private static final Log log = LogFactory.getLog(PeerListener.class);

    private TServerTransport serverTransport;
    private TServer server;
    private NodeContext nodeContext;


    /**
     * @param processor
     * @param nodeContext
     */
    public PeerListener(Siyapath.Processor processor, NodeContext nodeContext) throws TException {
        this.nodeContext = nodeContext;
        initializeThriftServer(processor);
    }

    /**
     * Creates a Thrift server instance
     * @param processor
     */
    private void initializeThriftServer(Siyapath.Processor processor) throws TTransportException {
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
