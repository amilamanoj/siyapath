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
import org.siyapath.service.*;
import org.siyapath.service.NodeStatus;
import org.siyapath.ui.NodeGUI;
import org.siyapath.ui.NodeUIHandler;
import org.siyapath.utils.CommonUtils;

import javax.swing.*;

/**
 * The starting point for a node
 */
public class SiyapathNode {

    private static final Log log = LogFactory.getLog(SiyapathNode.class);

    private SiyapathService handler;
    private Siyapath.Processor processor;
    private PeerListener peerListener;
    private PeerWorker peerWorker;
    private NodeContext nodeContext;


    public SiyapathNode(NodeInfo nodeInfo) {
        nodeContext = new NodeContext(nodeInfo);
        handler = new SiyapathService(nodeContext);
        processor = new Siyapath.Processor(handler);
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        NodeInfo nodeInfo = new NodeInfo();
        boolean showUI = true;
        if (args.length == 1) {
            if (args[0].equals("bs")) {
                nodeInfo.setBootstrapper(true);
                nodeInfo.setPort(CommonUtils.getBootstrapperPort());
                showUI = false;
            } else if (args[0].equals("cl")) {
                showUI = false;
            }
        }
        SiyapathNode node = new SiyapathNode(nodeInfo);

        if (showUI) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (UnsupportedLookAndFeelException e) {
            } catch (ClassNotFoundException e) {
            } catch (InstantiationException e) {
            } catch (IllegalAccessException e) {
            }
            node.getNodeContext().setGuiEnabled(true);
            NodeGUI gui = new NodeGUI();
            gui.setVisible(true);
            NodeUIHandler uiHandler = new NodeUIHandler(node.getNodeContext(), gui);
            uiHandler.startMonitor();
        }
        node.startSiyapathNode();
    }

    public void startSiyapathNode() {
        nodeContext.getNodeResource().setNodeStatus(NodeStatus.STARTING);
        log.info("Initializing Siyapath Node: " + nodeContext.getNodeInfo());

        if (nodeContext.isBootstrapper()) {
            log.info("Starting Bootstrapper Node...");

        }

        try {
            peerListener = new PeerListener(processor, nodeContext);
            peerListener.start();
            peerWorker = new PeerWorker(nodeContext);
            nodeContext.getNodeResource().setNodeStatus(NodeStatus.IDLE);
            peerWorker.start();
        } catch (TException e) {
            log.fatal("Error starting Siyapath Node. Check the configuration " + e.getMessage());
        }
    }


    public NodeContext getNodeContext() {
        return nodeContext;
    }

}
