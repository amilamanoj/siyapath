package org.siyapath;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.siyapath.utils.CommonUtils;

import java.net.ConnectException;
import java.util.Set;


public class PeerWorker {

    private static final Log log = LogFactory.getLog(PeerWorker.class);

    private NodeContext nodeContext = NodeContext.getInstance();

    public PeerWorker() {
    }

    public void start() {
        new WorkerThread().start();
    }

    private void initiateMembers() {
        TTransport transport = new TSocket("localhost", FrameworkInformation.BOOTSTRAP_PORT);
        try {
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            Siyapath.Client client = new Siyapath.Client(protocol);
            Set<NodeInfo> newNodes = CommonUtils.deSerialize(client.getMembers());
            nodeContext.updateMemberSet(newNodes);


        } catch (TTransportException e) {
            if (e.getCause() instanceof ConnectException) {
                System.out.println("Could not connect to the bootstrapper :(");
            }

        } catch (TException e) {
            e.printStackTrace();

        } finally {
            transport.close();
        }
    }

    /**
     * Select a random member form the known list and gossip current member list
     * by calling memberDiscovery service &
     * update the current list with the response
     */
    private void memberGossiper() {

        NodeInfo randomMember = nodeContext.getRandomMember();
        log.info("Getting a random member to gossip:" + randomMember);
        if (randomMember != null) {
            TTransport transport = new TSocket("localhost", randomMember.getPort());
            try {
                transport.open();
                TProtocol protocol = new TBinaryProtocol(transport);
                Siyapath.Client client = new Siyapath.Client(protocol);
                Set<NodeInfo> discoveredNodes = CommonUtils.deSerialize(client.memberDiscovery(CommonUtils.serialize(nodeContext.getMemberSet())));
                nodeContext.updateMemberSet(discoveredNodes);
                log.info("members Fetched:");
                for (NodeInfo i : discoveredNodes) {
                    log.info(i);
                }

            } catch (TTransportException e) {
                if (e.getCause() instanceof ConnectException) {
                    System.out.println("Could not connect to the bootstrapper :(");
                }

            } catch (TException e) {
                e.printStackTrace();

            } finally {
                transport.close();
            }
        }
    }

    private class WorkerThread extends Thread {

        public boolean isRunning = false;

        @Override
        public void run() {

            isRunning = true;
            while (isRunning) {
                if (!nodeContext.isBootstrapper()) {
                    if (!nodeContext.membersExist()) {
                        log.info("No known members. Contacting the bootstrapper to get some nodes");
                        initiateMembers();
                    }

                    memberGossiper();
                }
                log.info("Number of members: " + nodeContext.getMemberCount());
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
