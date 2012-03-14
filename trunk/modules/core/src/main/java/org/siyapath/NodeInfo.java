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
}
