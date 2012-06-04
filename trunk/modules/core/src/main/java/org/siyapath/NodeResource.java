package org.siyapath;

import java.util.HashMap;

import org.siyapath.monitor.SigarSystemInformation;

/**
 * NodeResource retrieves Node's SystemInformation
 */
public class NodeResource {
    private NodeInfo nodeInfo;
    private HashMap<String, String> nodeProperties;

    /* public static void main(String[] args) {
      NodeResource r = new NodeResource();
      System.out.print(r.getNodeProperties().get(SiyapathConstants.MEMORY_INFO));
  }  */

    public NodeResource() {
        nodeProperties = new HashMap<String, String>();
        initNodeProperties();
    }

    public NodeResource(NodeInfo nodeInfo) {
        nodeProperties = new HashMap<String, String>();
        initNodeProperties();
        this.nodeInfo = nodeInfo;

    }

    private void initNodeProperties() {
        nodeProperties.put(SiyapathConstants.CPU_INFO, getCPUInfo());
        nodeProperties.put(SiyapathConstants.MEMORY_INFO, getMemoryInfo());

    }

    /**
     * @return CPUInfo
     */
    private String getCPUInfo() {
        return SigarSystemInformation.getCPUInformation();
    }

    /**
     * @return MemoryInfo
     */
    private String getMemoryInfo() {
        return SigarSystemInformation.getMemoryInformation();
    }

    /**
     * @param nodeProperties
     */
    public void setNodeProperties(HashMap<String, String> nodeProperties) {
        this.nodeProperties = nodeProperties;
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

        return nodeInfo;
    }

    /**
     * @return nodeProperties
     */
    public HashMap<String, String> getNodeProperties() {
        return nodeProperties;
    }

}
