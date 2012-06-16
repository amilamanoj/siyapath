package org.siyapath;

import org.siyapath.utils.CommonUtils;

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

    /**
     *
     * @return port number
     */
    public int getPort() {
        return port;
    }

    /**
     *
     * @param port
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     *
     * @return node ID
     */
    public int getNodeId() {
        return nodeId;
    }

    /**
     *
     * @param nodeId
     */
    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

    /**
     *
     * @return ip address
     */
    public String getIp() {
        return ip;
    }

    /**
     *
     * @param ip
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     *
     * @return true if bootstrapper false otherwise
     */
    public boolean isBootstrapper() {
        return bootstrapper;
    }

    /**
     *
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
        return nodeId;
    }

    @Override
    public String toString() {
        return "Node-id:" + nodeId + "-port:" + port;
    }
}
