package org.siyapath;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hyperic.sigar.SigarException;

import org.siyapath.monitor.SigarSystemInformation;
import org.siyapath.service.NodeStatus;
import org.siyapath.service.ResourceLevel;

import java.util.Date;


/**
 * NodeResource retrieves Node's SystemInformation
 */
public final class NodeResource {
    private final Log log = LogFactory.getLog(NodeResource.class);

    private NodeInfo nodeInfo;
    private ResourceLevel resourceLevel;
    private NodeStatus nodeStatus;
    private long timeStamp;

    public Long getTimeStamp() {
        return timeStamp;
    }

    private void updateTimeStamp(){
        Date date= new Date();
        timeStamp=date.getTime();
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }


    public ResourceLevel getResourceLevel() {
        return resourceLevel;
    }

    public void setResourceLevel(ResourceLevel resourceLevel) {
        this.resourceLevel = resourceLevel;
    }


    public NodeResource(NodeInfo nodeInfo) {
        initResourceLevel();
        updateTimeStamp();
        this.nodeInfo = nodeInfo;
        setNodeStatus(NodeStatus.CREATED);

    }


    public NodeResource() {
    }


    public synchronized void setNodeStatus(org.siyapath.service.NodeStatus nodeStatus) {
        this.nodeStatus = nodeStatus;
    }

    public synchronized NodeStatus getNodeStatus() {
        return nodeStatus;
    }


    public synchronized boolean isIdle() {
        return nodeStatus == NodeStatus.IDLE;
    }

    public NodeResource refreshNodeResource() {
        this.initResourceLevel();
        this.updateTimeStamp();
        return this;
    }

    private void initResourceLevel() {
        int freeMemory = 0;
        try {
            freeMemory = SigarSystemInformation.getFreeMemoryInfo();
        } catch (SigarException e) {
            log.error("Error retrieving memory info: " + e.getMessage());
        }
        if (freeMemory < 2048) {
            setResourceLevel(ResourceLevel.LOW);
        } else if (2048 < freeMemory && freeMemory < 6144) {
            setResourceLevel(ResourceLevel.MEDIUM);
        } else if (6144 < freeMemory) {
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


    @Override
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

    @Override
    public int hashCode() {
        return this.getNodeInfo().getNodeId();
    }
}
