package org.siyapath;


import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.net.ConnectException;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: amila
 * Date: 2/6/12
 * Time: 9:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class PeerWorker {

    private NodeContext nodeContext;

    public PeerWorker(NodeContext nodeContext) {
        this.nodeContext = nodeContext;
    }

    public void start() {
        new WorkerThread().start();
    }

    private void initiateMembers() {
        TTransport transport = new TSocket("localhost", FrameworkInformation.BOOTSTRAP_PORT);
        try {
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            GossipService.Client client = new GossipService.Client(protocol);
            Set newNodes = client.getMembers();
            for (Object n: newNodes){
                String node = (String) n;
                nodeContext.addMember(Integer.parseInt(node));
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

    private class WorkerThread extends Thread {

        public boolean isRunning = false;

        @Override
        public void run() {

            isRunning = true;
            while (isRunning) {
                if (!nodeContext.isBootstrapper()){
                if (!nodeContext.membersExist()) {
                    System.out.println("No known members. Contacting the bootstrapper to get some nodes");
                    initiateMembers();
                }
                }
                System.out.println("Number of members: " + nodeContext.getMemeberCount());
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
