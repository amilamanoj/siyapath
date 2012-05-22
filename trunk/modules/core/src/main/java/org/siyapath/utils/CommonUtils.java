package org.siyapath.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.siyapath.NodeInfo;
import org.siyapath.service.*;

import java.net.*;
import java.util.*;

public class CommonUtils {
    
    private static Log log = LogFactory.getLog(CommonUtils.class);

    //temporary testing purposes

    /**
     *
     * @return InetSocketAddress for given host and port
     */
    public static InetSocketAddress getRandomAddress(){
        int port = 9090;
        String host="10.1.";
        host = host + new Random().nextInt(255) + ".";
        host = host + new Random().nextInt(255) ;
        InetSocketAddress address = new InetSocketAddress(host, port);
        return address;
    }

    /**
     *
     * @return a random port
     */
    public static int getRandomPort(){
        return new Random().nextInt(1000) + 9021;
    }

    /**
     *
     * @return ip address
     */
    public static String getIPAddress()  {
        List<InetAddress> ipAddresses = new ArrayList<InetAddress>();
        String ipAddress = null;

        Enumeration e = null;
        try {
            e = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e1) {
            e1.printStackTrace();
        }
        while (e.hasMoreElements()) {
            NetworkInterface ni = (NetworkInterface) e.nextElement();
            // if (ni.isLoopback() || !ni.isUp()) continue;

            Enumeration e2 = ni.getInetAddresses();
            while (e2.hasMoreElements()) {
                InetAddress ip = (InetAddress) e2.nextElement();
                ipAddresses.add(ip);
            }
        }
        if (ipAddresses.isEmpty()) {
            return null;
        } else {
            for (InetAddress ip : ipAddresses) {
                if (ip instanceof Inet4Address) {
                    ipAddress = ip.getHostAddress();
                    break;
                }
            }
        }

        if (ipAddress == null) {
            ipAddress = ipAddresses.get(0).getHostAddress();
        }

        return ipAddress;
    }

    /**
     *
     * @param i
     * @return a random number
     */
    public static int getRandomNumber(int i) {
        return new Random().nextInt(i);
    }

    /**
     *
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
     *
     * @param nodeInfos
     * @return Set of NodeData for given Set of node info
     */
    public static Set<NodeData> serialize(Set<NodeInfo> nodeInfos) {
        Set<NodeData> nodeDatas = new HashSet<NodeData>();
        for (NodeInfo ni: nodeInfos) {
            nodeDatas.add(serialize(ni));
        }
        return nodeDatas;
    }

    /**
     *
     * @param nodeData
     * @return NodeInfo for given nodeData
     */
    public static NodeInfo deSerialize(NodeData nodeData) {
        NodeInfo node = new NodeInfo();
        node.setNodeId(nodeData.getNodeID());
        node.setIp(nodeData.getIp());
        node.setPort(nodeData.getPort());
        return node;
    }

    /**
     *
     * @param nodeDatas
     * @return set of NodeInfo for given set of nodeData
     */
    public static Set<NodeInfo> deSerialize(Set<NodeData> nodeDatas) {
        Set<NodeInfo> nodeInfos = new HashSet<NodeInfo>();
        for (NodeData nd: nodeDatas) {
            nodeInfos.add(deSerialize(nd));
        }
        return nodeInfos;
    }
}
