package org.siyapath;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.siyapath.gossip.GossipImpl;
import org.siyapath.utils.CommonUtils;
import org.siyapath.service.*;

import java.net.ConnectException;
import java.util.Set;


public class PeerWorker {

    private static final Log log = LogFactory.getLog(PeerWorker.class);

    private NodeContext nodeContext;

    public PeerWorker(NodeContext nodeContext) {
        this.nodeContext = nodeContext;
    }

    public void start() {
        new WorkerThread("ListenerThread-" + nodeContext.getNodeInfo().toString()).start();
    }

    private void initiateMembers() {
        TTransport transport = new TSocket("localhost", FrameworkInformation.BOOTSTRAP_PORT);
        try {
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            Siyapath.Client client = new Siyapath.Client(protocol);
            Set<NodeInfo> newNodes = CommonUtils.deSerialize(client.getMembers());
            nodeContext.updateMemberSet(newNodes);  //TODO: remove self

        } catch (TTransportException e) {
            if (e.getCause() instanceof ConnectException) {
                log.error("Could not connect to the bootstrapper :(");
            }

        } catch (TException e) {
            e.printStackTrace();

        } finally {
            transport.close();
        }
    }

    private class WorkerThread extends Thread {

        public boolean isRunning = false;

        private WorkerThread(String name) {
            super(name);
        }

        @Override
        public void run() {

            isRunning = true;
            while (isRunning) {
                if (!nodeContext.isBootstrapper()) {
                    if (!nodeContext.membersExist()) {
                        log.info("No known members. Contacting the bootstrapper to get some nodes");
                        initiateMembers();
                    } else {
                        log.info("Number of known members: " + nodeContext.getMemberCount());
                        new GossipImpl(nodeContext).memberGossiper();
                        new GossipImpl(nodeContext).resourceGossiper();
                    }
                }
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
