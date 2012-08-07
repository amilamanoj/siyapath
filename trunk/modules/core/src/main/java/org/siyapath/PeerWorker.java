package org.siyapath;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
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
    private GossipImpl gossiper;

    public PeerWorker(NodeContext nodeContext) {
        this.nodeContext = nodeContext;
        this.gossiper = new GossipImpl(nodeContext);
    }

    public void start() {
        new WorkerThread("ListenerThread-" + nodeContext.getNodeInfo().toString()).start();
    }

    /**
     * @return true if a bootstrapper node exists, false otherwise
     */
    private boolean notifyPresence() {
        boolean presenceNotified = false;
        log.debug("Trying to connect to a bootstrapper node");
        //TTransport transport = new TFramedTransport(new TSocket("localhost", FrameworkInformation.BOOTSTRAP_PORT));
        TTransport transport = new TSocket("localhost", FrameworkInformation.BOOTSTRAP_PORT);
        try {
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            Siyapath.Client client = new Siyapath.Client(protocol);
            presenceNotified = client.notifyPresence(CommonUtils.serialize(nodeContext.getNodeInfo()));
        } catch (TTransportException e) {
//            e.printStackTrace();
            if (e.getCause() instanceof ConnectException) {
                log.debug("Could not connect to the bootstrapper :(");
            }
        } catch (TException e) {
            e.printStackTrace();
        } finally {
            transport.close();
        }
        return presenceNotified;
    }

    private void initiateMembers() {
        TTransport transport = new TSocket("localhost", FrameworkInformation.BOOTSTRAP_PORT);
        //TTransport transport = new TFramedTransport(new TSocket("localhost", FrameworkInformation.BOOTSTRAP_PORT));
        try {
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            Siyapath.Client client = new Siyapath.Client(protocol);
            Set<NodeInfo> newNodes = CommonUtils.deSerialize(client.getMembers());
            nodeContext.updateMemberSet(removeSelf(newNodes));

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

    private Set<NodeInfo> removeSelf(Set<NodeInfo> nodes) {
        if (nodes.contains(nodeContext.getNodeInfo())) {
            nodes.remove(nodeContext.getNodeInfo());
        }
        return nodes;
    }

    private class WorkerThread extends Thread {

        public boolean isRunning = false;

        private WorkerThread(String name) {
            super(name);
        }

        @Override
        public void run() {

            isRunning = true;
            nodeContext.setWorkerEnabled(true);
            while (isRunning) {
                if (!nodeContext.isBootstrapper()) {
                    if (!nodeContext.isPresenceNotified()) {
                        log.debug("Notifying presence to bootstrapper node");
                        nodeContext.setPresenceNotified(notifyPresence());
                    } else if (!nodeContext.membersExist()) {
                        log.debug("No known members. Contacting the bootstrapper to get some nodes");
                        initiateMembers();
                    } else {
                        gossiper.memberGossiper();
                        gossiper.resourceGossiper();
                    }
                    log.info("Number of known members: " + nodeContext.getMemberCount());
                }
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void shutDown() {
            isRunning = false;
            nodeContext.setWorkerEnabled(false);
        }
    }
}
