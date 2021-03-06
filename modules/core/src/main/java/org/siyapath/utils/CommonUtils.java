package org.siyapath.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.siyapath.NodeInfo;
import org.siyapath.NodeResource;
import org.siyapath.SiyapathConstants;
import org.siyapath.service.NodeData;
import org.siyapath.service.NodeResourceData;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.*;

public final class CommonUtils {

    private static Log log = LogFactory.getLog(CommonUtils.class);

    private static String bootsrapperIp="localhost";
    private static int bootsrapperPort= 9020;


static {

    File propertiesFile = new File("siyapath.properties");
    Properties siyapathProps = new Properties();
        try {
            InputStream stream =new FileInputStream(propertiesFile);
            siyapathProps.load(stream);
            bootsrapperIp = siyapathProps.getProperty("siyapath.bootsrapper.ip");
            bootsrapperPort = Integer.parseInt(siyapathProps.getProperty("siyapath.bootsrapper.port"));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("No properties found");

        }  catch (IOException e) {
             e.printStackTrace();
        }


  }

    private CommonUtils() {
    }

    /**
     * @return InetSocketAddress for given host and port
     */
    public static InetSocketAddress getRandomAddress() {
        int port = 9090;
        String host = "10.1.";
        host = host + new Random().nextInt(255) + ".";
        host = host + new Random().nextInt(255);
        return new InetSocketAddress(host, port);
    }

    /**
     * @return a random port
     */
    public static int getRandomPort() {
        return new Random().nextInt(1000) + 9021;
    }

    /**
     * @return ip address
     */
    public static String getIPAddress() {

        if (SiyapathConstants.LOCAL_TEST) return "localhost";

        List<InetAddress> ipAddresses = new ArrayList<InetAddress>();
        String ipAddress = null;

        Enumeration e;
        try {
            e = NetworkInterface.getNetworkInterfaces();

            while (e.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) e.nextElement();
                if (ni.isLoopback() || !ni.isUp()) continue;

                for (Enumeration e2 = ni.getInetAddresses(); e2.hasMoreElements(); ) {
                    InetAddress ip = (InetAddress) e2.nextElement();
                    ipAddresses.add(ip);
                }
            }
        } catch (SocketException e1) {
            e1.printStackTrace();
        }

        if (!ipAddresses.isEmpty()) {
            for (InetAddress ip : ipAddresses) {
                if (ip instanceof Inet4Address) {
                    ipAddress = ip.getHostAddress();
                    break;
                }
            }
            if (ipAddress == null) {
                ipAddress = ipAddresses.get(0).getHostAddress();
            }
        }

        return ipAddress;
    }

    public static String getBootstrapperIP() {
        if (SiyapathConstants.LOCAL_TEST) return "localhost";
        return bootsrapperIp;
    }

    public static int getBootstrapperPort() {
        if (SiyapathConstants.LOCAL_TEST) return 9020;
        return bootsrapperPort;
    }

    /**
     * @param i
     * @return a random number
     */
    public static int getRandomNumber(int i) {
        return new Random().nextInt(i);
    }

    /**
     * @param nodeInfo
     * @return NodeData for specified node info
     */
    public static NodeData serialize(NodeInfo nodeInfo) {
        NodeData node = new NodeData();
        node.setNodeID(nodeInfo.getNodeId());
        node.setIp(nodeInfo.getIp());
        node.setPort(nodeInfo.getPort());
        return node;
    }

    /**
     * @param nodeResource
     * @return NodeResourceData for retrieved node resource
     */
    public static NodeResourceData serialize(NodeResource nodeResource) {
        NodeResourceData resourceData = new NodeResourceData();
        resourceData.setResourceLevel(nodeResource.getResourceLevel());
        resourceData.setNodeData(serialize(nodeResource.getNodeInfo()));
        resourceData.setNodeStatus(nodeResource.getNodeStatus());
        resourceData.setTimeStamp(nodeResource.getTimeStamp());
        return resourceData;
    }

    /**
     * @param nodeInfos
     * @return Set of NodeData for given Set of node info
     */
    public static Set<NodeData> serialize(Set<NodeInfo> nodeInfos) {
        Set<NodeData> nodeDatas = new HashSet<NodeData>();
        for (NodeInfo ni : nodeInfos) {
            nodeDatas.add(serialize(ni));
        }
        return nodeDatas;
    }

    /**
     *
     * @param resourceNodes
     * @return resourceNodesData
     */
    public static Map<Integer, NodeResourceData> serialize(Map<Integer, NodeResource> resourceNodes) {
        Map<Integer, NodeResourceData> resourceNodesData = new HashMap<Integer, NodeResourceData>();

        for (Map.Entry<Integer, NodeResource> entry : resourceNodes.entrySet()) {
            int key = entry.getKey();
            NodeResource value = entry.getValue();
            resourceNodesData.put(key, serialize(value));
        }

        return resourceNodesData;
    }

    /**
     * @param nodeData
     * @return NodeInfo for given nodeData
     */
    public static NodeInfo deSerialize(NodeData nodeData) {
        return new NodeInfo(nodeData.getNodeID(), nodeData.getIp(), nodeData.getPort());
    }

    /**
     * @param nodeResourceData
     * @return nodeResource for given nodeResourceData
     */
    public static NodeResource deSerialize(NodeResourceData nodeResourceData) {
        NodeResource nodeResource = new NodeResource();
        nodeResource.setResourceLevel((nodeResourceData.getResourceLevel()));
        nodeResource.setNodeInfo(deSerialize(nodeResourceData.getNodeData()));
        nodeResource.setNodeStatus(nodeResourceData.getNodeStatus());
        nodeResource.setTimeStamp(nodeResourceData.getTimeStamp());
        return nodeResource;
    }

    /**
     * @param nodeDatas
     * @return set of NodeInfo for given set of nodeData
     */
    public static Set<NodeInfo> deSerialize(Set<NodeData> nodeDatas) {
        Set<NodeInfo> nodeInfos = new HashSet<NodeInfo>();
        for (NodeData nd : nodeDatas) {
            nodeInfos.add(deSerialize(nd));
        }
        return nodeInfos;
    }

    /**
     * @param resourceNodesData
     * @return map of NodeResource for given Map of NodeResourceData
     */
    public static Map<Integer, NodeResource> deSerialize(Map<Integer, NodeResourceData> resourceNodesData) {
        Map<Integer, NodeResource> resourceNodes = new HashMap<Integer, NodeResource>();

        for (Map.Entry<Integer, NodeResourceData> entry : resourceNodesData.entrySet()) {
            int key = entry.getKey();
            NodeResourceData value = entry.getValue();
            resourceNodes.put(key, deSerialize(value));
        }

        return resourceNodes;
    }

    /**
     * @return ByteBuffer for byte array from given byte-code
     * @throws java.io.IOException
     */
    public static ByteBuffer convertFileToByteBuffer(String fileName) throws IOException {

        File file = new File(fileName);
        InputStream inputStream = null;

        byte[] bytes = new byte[(int) file.length()];
        if (file.length() > Integer.MAX_VALUE) {
            log.error("File is too large.");
        }

        try {
//            bytes = new byte[(int)file.length()];  TODO: max file size?
//            if (file.length() > Integer.MAX_VALUE) {
//                log.error("File is too large.");
//            }
            inputStream = new BufferedInputStream(new FileInputStream(file));
            int offset = 0, numRead;

            while (offset < bytes.length
                    && (numRead = inputStream.read(bytes, offset, bytes.length - offset)) >= 0) {
                offset += numRead;
            }
            // Ensure all the bytes have been read
            if (offset < bytes.length) {
                log.warn("Could not completely read file " + file.getName());
                throw new IOException("Could not completely read file " + file.getName());
            } else {
                log.info("Successfully located and read binary.");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return ByteBuffer.wrap(bytes);
    }
}
