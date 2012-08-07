package org.siyapath;

import org.siyapath.service.NodeStatus;
import org.siyapath.utils.CommonUtils;

public class NodeInfo {
    private int nodeId;
    private String ip;
    private int port;
    private boolean bootstrapper;
    private org.siyapath.service.NodeStatus nodeStatus;

    public void setNodeStatus(org.siyapath.service.NodeStatus nodeStatus) {
        this.nodeStatus = nodeStatus;
    }


    /* public enum NodeStatus {
       CREATED(1) ,
       STARTING (2),
       BUSY (3),
       IDLE(4);
        int ind;
       NodeStatus(int i) {
           ind=i;
       }
   } */

    public NodeInfo() {
        ip = CommonUtils.getIPAddress();
        port = CommonUtils.getRandomPort();
        nodeId = CommonUtils.getRandomNumber(10000); //TODO: make the Id unique
        this.bootstrapper = false;
        setNodeStatus(NodeStatus.CREATED);

    }

    public NodeStatus getNodeStatus() {
        return nodeStatus;
    }


    public boolean isIdle() {
        if (nodeStatus == NodeStatus.IDLE) {
            return true;
        } else {
            return false;
        }
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
