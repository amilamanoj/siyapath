package org.siyapath;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hyperic.sigar.SigarException;

import org.siyapath.monitor.SigarSystemInformation;
import org.siyapath.service.NodeStatus;
import org.siyapath.service.ResourceLevel;

import java.util.Date;


/**
 * NodeResource class holds system monitoring information, current node status and timestamp
 */
public final class NodeResource {
    private final Log log = LogFactory.getLog(NodeResource.class);

    private NodeInfo nodeInfo;
    private ResourceLevel resourceLevel;
    private NodeStatus nodeStatus;
    private long timeStamp;

    /**
     * Default Constructor for NodeResource instance
     */
     public NodeResource() {
    }

    /**
     * Constructor for NodeResource instance with parameter initialization
     *
     * @param nodeInfo
     */
    public NodeResource(NodeInfo nodeInfo) {
        initResourceLevel();
        updateTimeStamp();
        this.nodeInfo = nodeInfo;
        setNodeStatus(NodeStatus.CREATED);

    }

    /**
     *
     * @return timeStamp
     */
    public Long getTimeStamp() {
        return timeStamp;
    }

    /**
     * Update the timeStamp instance
     */
    private void updateTimeStamp(){
        Date date= new Date();
        timeStamp=date.getTime();
    }

    /**
     * set the timeStamp
     *
     * @param timeStamp
     */
    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    /**
     * get current resource level
     *
     * @return  resourceLevel, a ResourceLevel enum value
     */
    public ResourceLevel getResourceLevel() {
        return resourceLevel;
    }

    /**
     * set resource level
     *
     * @param resourceLevel
     */
    public void setResourceLevel(ResourceLevel resourceLevel) {
        this.resourceLevel = resourceLevel;
    }

    /**
     *  set node status
     *
     * @param nodeStatus
     */
    public synchronized void setNodeStatus(NodeStatus nodeStatus) {
        this.nodeStatus = nodeStatus;
    }

    /**
     *
     * @return nodeStatus
     */
    public NodeStatus getNodeStatus() {
        return nodeStatus;
    }

    /**
     *
     * @return boolean , whether node is idle
     */
    public boolean isIdle() {
        return nodeStatus == NodeStatus.IDLE;
    }

    /**
     * update nodeResource information
     *
     * @return NodeResource instance
     */
    public NodeResource refreshNodeResource() {
        this.initResourceLevel();
        this.updateTimeStamp();
        return this;
    }

    /**
     *
     * initializes system resource level
     */
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
