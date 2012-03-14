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

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getNodeId() {
        return nodeId;
    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public boolean isBootstrapper() {
        return bootstrapper;
    }

    public void setBootstrapper(boolean bootstrapper) {
        this.bootstrapper = bootstrapper;
    }
}
