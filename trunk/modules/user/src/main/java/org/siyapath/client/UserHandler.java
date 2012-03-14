package org.siyapath.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.siyapath.FrameworkInformation;
import org.siyapath.NodeInfo;
import org.siyapath.Siyapath;
import org.siyapath.Task;
import org.siyapath.utils.CommonUtils;

import java.io.*;
import java.net.ConnectException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class UserHandler {
    private static final Log log = LogFactory.getLog(UserHandler.class);

    private NodeInfo nodeInfo;

    public UserHandler() {
        this.nodeInfo = new NodeInfo();
        nodeInfo.setPort(CommonUtils.getRandomPort());
    }

    public String authenticate(String username, String password) {
        String res = null;
        TTransport transport = new TSocket("localhost", FrameworkInformation.BOOTSTRAP_PORT);
        try {
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            Siyapath.Client client = new Siyapath.Client(protocol);
            res = client.userLogin(username, password);
        } catch (TTransportException e) {
            if (e.getCause() instanceof ConnectException) {
                res = "connecEx";
                e.printStackTrace();
            }
        } catch (TException e) {
            res = "tEx";
            e.printStackTrace();
        } finally {
            transport.close();
        }
        return res;
    }

    public void submitJob(String fileName) {
        NodeInfo selectedNode = getDistributorNode();
        if (selectedNode != null) {
            sendJob(selectedNode);
        } else {
            log.warn("Could not get a distributor node");
        }
    }

    private NodeInfo getDistributorNode() {
//        String res = null;
        NodeInfo selectedMember = null;
        TTransport transport = new TSocket("localhost", FrameworkInformation.BOOTSTRAP_PORT);
        try {
            log.info("Getting list of members from bootstrap");
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            Siyapath.Client client = new Siyapath.Client(protocol);
            Object memberArray[] = client.getMembers().toArray();
            log.info("Number of members from bootstrapper: " + memberArray.length);
            selectedMember = (NodeInfo) memberArray[new Random(memberArray.length).nextInt()];
        } catch (TTransportException e) {
            if (e.getCause() instanceof ConnectException) {
//                res = "connecEx";
                e.printStackTrace();
            }
        } catch (TException e) {
//            res = "tEx";
            e.printStackTrace();
        } finally {
            transport.close();
        }
        log.info("Selected node: " + selectedMember);

        return selectedMember;
    }

    private void sendJob(NodeInfo node) {
        TTransport transport = new TSocket("localhost", node.getPort());
        Task task = new Task(123, 234, ByteBuffer.wrap(new byte[10]), "Sending a Temp task data in a String.", "org.test.siyapath.CalcDemo",  CommonUtils.serialize(nodeInfo), null);
        Map taskList = new HashMap<Integer, Task>();
        taskList.put(1, task);
        try {
            log.info("Sending new job to node: " + node.getPort());
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            Siyapath.Client client = new Siyapath.Client(protocol);
            client.submitJob(00001, CommonUtils.serialize(nodeInfo), taskList);
        } catch (TTransportException e) {
            if (e.getCause() instanceof ConnectException) {
                e.printStackTrace();
            }
        } catch (TException e) {
            e.printStackTrace();
        } finally {
            transport.close();
        }
    }

    public ByteBuffer convertFileToByteBuffer() throws IOException {

        /*temporary location has been set*/
        final String BINARY_FILE_NAME = "C:\\Development\\CalcDemo.class";
        File file = new File(BINARY_FILE_NAME);
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
