package org.siyapath;

import java.util.HashMap;

import org.siyapath.monitor.SigarSystemInformation;
import org.siyapath.service.NodeStatus;
import org.siyapath.service.ResourceLevel;


/**
 * NodeResource retrieves Node's SystemInformation
 */
public class NodeResource {
    private NodeInfo nodeInfo;

    public ResourceLevel getResourceLevel() {
        return resourceLevel;
    }

    public void setResourceLevel(ResourceLevel resourceLevel) {
        this.resourceLevel = resourceLevel;
    }

    private ResourceLevel resourceLevel;
    private NodeStatus nodeStatus;

    public NodeResource(NodeInfo nodeInfo) {
        initResourceLevel();
        this.nodeInfo = nodeInfo;
        setNodeStatus(NodeStatus.CREATED);

    }


    public NodeResource() {
        initResourceLevel();
    }


    public synchronized void setNodeStatus(org.siyapath.service.NodeStatus nodeStatus) {
        this.nodeStatus = nodeStatus;
    }

    public synchronized NodeStatus getNodeStatus() {
        return nodeStatus;
    }


    public synchronized boolean isIdle() {
        if (nodeStatus == NodeStatus.IDLE) {
            return true;
        } else {
            return false;
        }
    }

    public NodeResource refreshResourceLevel(){
        this.initResourceLevel();
        return this;
    }

    private void initResourceLevel() {
        if (SigarSystemInformation.getFreeMemoryInfo() < 2048) {
            setResourceLevel(ResourceLevel.LOW);
        } else if (2048 < SigarSystemInformation.getFreeMemoryInfo() && SigarSystemInformation.getFreeMemoryInfo() < 6144) {
            setResourceLevel(ResourceLevel.MEDIUM);
        } else if (6144 < SigarSystemInformation.getFreeMemoryInfo()) {
            setResourceLevel(ResourceLevel.HIGH);
        }

    }

    /**
     * @param nodeInfo
     */
    public void setNodeInfo(NodeInfo nodeInfo) {
        this.nodeInfo = nodeInfo;
    }

    /**
     * @return nodeInfo
     */
    public NodeInfo getNodeInfo() {

        return this.nodeInfo;
    }



    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        NodeResource other = (NodeResource) obj;
        return getNodeInfo().getNodeId() == other.getNodeInfo().getNodeId();
    }

}
