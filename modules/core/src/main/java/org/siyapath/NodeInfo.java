package org.siyapath;

import org.siyapath.utils.CommonUtils;

public class NodeInfo {
    private int nodeId;
    private String ip;
    private int port;
    private boolean bootStrapper;

    public NodeInfo() {
        ip = CommonUtils.getIPAddress();
        port = CommonUtils.getRandomPort();
        this.bootStrapper = false;

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

    public boolean isBootStrapper() {
        return bootStrapper;
    }

    public void setBootStrapper(boolean bootStrapper) {
        this.bootStrapper = bootStrapper;
    }
}
