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
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.siyapath.gossip.GossipImpl;
import org.siyapath.service.Siyapath;
import org.siyapath.utils.CommonUtils;

import java.util.Set;

/**
 * Worker component of the peer. Performs periodic communications.
 */
public class PeerWorker {

    private static final Log log = LogFactory.getLog(PeerWorker.class);

    private NodeContext nodeContext;
    private GossipImpl gossiper;

    public PeerWorker(NodeContext nodeContext) {
        this.nodeContext = nodeContext;
        this.gossiper = new GossipImpl(nodeContext);
    }

    public void start() {
        new WorkerThread("WorkerThread-" + nodeContext.getNodeInfo().toString()).start();
    }

    /**
     * @return true if a bootstrapper node exists, false otherwise
     */
    private boolean notifyPresence() {
        boolean presenceNotified = false;
        log.debug("Trying to connect to a bootstrapper node");
        //TTransport transport = new TFramedTransport(new TSocket("localhost", FrameworkInformation.BOOTSTRAP_PORT));
        TTransport transport = new TSocket(CommonUtils.getBootstrapperIP(), CommonUtils.getBootstrapperPort());
        TProtocol protocol = new TBinaryProtocol(transport);
        Siyapath.Client client = new Siyapath.Client(protocol);

        try {
            transport.open();
            presenceNotified = client.notifyPresence(CommonUtils.serialize(nodeContext.getNodeInfo()));
        } catch (TTransportException e) {
            log.error(e.getMessage());
        } catch (TException e) {
            log.error(e.getMessage());
        } finally {
            transport.close();
        }
        if (!presenceNotified)
        {
            log.error("Could not connect to the bootstrapper to notify presense");
        }
        return presenceNotified;
    }

    private void initiateMembers() {
        TTransport transport = new TSocket(CommonUtils.getBootstrapperIP(), CommonUtils.getBootstrapperPort());
        //TTransport transport = new TFramedTransport(new TSocket("localhost", FrameworkInformation.BOOTSTRAP_PORT));
        try {
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            Siyapath.Client client = new Siyapath.Client(protocol);
            Set<NodeInfo> newNodes = CommonUtils.deSerialize(client.getMembers());
            nodeContext.updateMemberSet(removeSelf(newNodes));

        } catch (TException e) {
            log.error("Could not connect to the bootstrapper to fetch nodes. " + e.getMessage());

        } finally {
            transport.close();
        }
    }

    /**
     * Remove this node from retrieved node set
     *
     * @param nodes
     * @return nodes
     */
    private Set<NodeInfo> removeSelf(Set<NodeInfo> nodes) {
        if (nodes.contains(nodeContext.getNodeInfo())) {
            nodes.remove(nodeContext.getNodeInfo());
        }
        return nodes;
    }

    private class WorkerThread extends Thread {

        private boolean isRunning = false;

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
                        /**Member Gossip*/
                        gossiper.memberGossiper();
                        /**Resource Gossip*/
                        gossiper.resourceGossiper();
                    }
                    log.info("Number of known members: " + nodeContext.getMemberCount());
                    log.info("Number of known membersWithResources: " + nodeContext.getMemberResourceMap().size());

                }
                try {
                    sleep(SiyapathConstants.GOSSIP_FREQUENCY_MILLIS);
                } catch (InterruptedException e) {
                }
            }
        }

        public void shutDown() {
            isRunning = false;
            nodeContext.setWorkerEnabled(false);
        }
    }
}
