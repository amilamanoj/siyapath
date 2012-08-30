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

import org.siyapath.utils.CommonUtils;

/**
 * Holds node information id, ip address and port
 */
public class NodeInfo {
    private int nodeId;
    private String ip;
    private int port;
    private boolean bootstrapper;


    public NodeInfo() {
        ip = CommonUtils.getIPAddress();
        port = CommonUtils.getRandomPort();
        nodeId = CommonUtils.getRandomNumber(10000); //TODO: make the Id unique
        this.bootstrapper = false;
    }

    public NodeInfo(int nodeId, String ip, int port) {
        this.nodeId = nodeId;
        this.ip = ip;
        this.port = port;
    }

    /**
     * @return port number
     */
    public int getPort() {
        return port;
    }

    /**
     * @param port
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * @return node ID
     */
    public int getNodeId() {
        return nodeId;
    }

    /**
     * @param nodeId
     */
    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

    /**
     * @return ip address
     */
    public String getIp() {
        return ip;
    }

    /**
     * @param ip
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * @return true if bootstrapper false otherwise
     */
    public boolean isBootstrapper() {
        return bootstrapper;
    }

    /**
     * @param bootstrapper
     */
    public void setBootstrapper(boolean bootstrapper) {
        this.bootstrapper = bootstrapper;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        NodeInfo other = (NodeInfo) obj;
        return nodeId == other.nodeId;
    }

    @Override
    public int hashCode() {
        return this.nodeId;
    }

    @Override
    public String toString() {
        return "NodeID:" + nodeId + " IP-" + ip + ":" + port;
    }
}
