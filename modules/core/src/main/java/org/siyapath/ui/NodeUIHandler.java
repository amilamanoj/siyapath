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

package org.siyapath.ui;

import org.siyapath.NodeContext;
import org.siyapath.NodeInfo;

public class NodeUIHandler {

    private NodeContext nodeContext;
    private NodeGUI ui;

    public NodeUIHandler(NodeContext context, NodeGUI ui) {
        this.ui = ui;
        this.nodeContext = context;
    }

    public void startMonitor() {
        new Thread(new UIUpdater()).start();
    }

    private class UIUpdater implements Runnable {
        private boolean active;

        @Override
        public void run() {
            active = true;

            while (active) {

                ui.setListenerStat(nodeContext.isListenerEnabled() ? "ON" : "OFF");
                ui.setWorkerStat(nodeContext.isWorkerEnabled() ? "ON" : "OFF");
                int memberCount = nodeContext.getMemberCount();
                ui.setMemberCount(String.valueOf(memberCount));
                StringBuilder members = new StringBuilder();

                if (memberCount < 1) {
                    members.append("<No known members>");
                } else {
                    members.append("<html>");
                    for (NodeInfo node : nodeContext.getMemberSet()) {
                        members.append(node.toString());
                        members.append("<br />");
                    }
                    members.append("</html>");
                }
                ui.setMembers(members.toString());

                ui.setNodeStatus(nodeContext.getNodeResource().getNodeStatus().name());
                ui.setTotalTasks(String.valueOf(nodeContext.getTotalTasks()));
                switch (nodeContext.getNodeResource().getNodeStatus()) {
                    case DISTRIBUTING:
                        ui.setProcessStatus("Jobs are being processed...");
                        ui.setProcessingInfo("");
                        break;
                    case PROCESSING_IDLE:
                    case PROCESSING_BUSY:
                        ui.setProcessStatus("Tasks are being processed...");
                        StringBuilder sb = new StringBuilder();
                        sb.append("<html>Task List:\n");
                        for (Integer taskId : nodeContext.getTaskIds()) {
                            sb.append(taskId);
                            sb.append("<br>");
                        }
                        ui.setProcessingInfo(sb.toString());
                        break;
                    case IDLE:
                        ui.setProcessStatus("Waiting for work...");
                        ui.setProcessingInfo("");
                        break;
                }

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                }
            }

        }
    }
}
