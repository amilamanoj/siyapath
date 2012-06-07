package org.siyapath.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.siyapath.*;
import org.siyapath.service.*;
import org.siyapath.utils.CommonUtils;

import java.io.*;
import java.net.ConnectException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class UserHandler {
    private static final Log log = LogFactory.getLog(UserHandler.class);

    private NodeContext context;

    public UserHandler() {
        this.context = new NodeContext();
    }

    /**
     *
     * @param username
     * @param password
     * @return String success if user can be authenticated, failure otherwise, or exception string
     */
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

    /**
     * selects a distributor nodes and sends the job
     * @param fileName path to file containing the job
     */
    public void submitJob(String fileName) {
        NodeInfo selectedNode = getDistributorNode();
        if (selectedNode != null) {
            sendJob(selectedNode, fileName);
        } else {
            log.warn("Could not get a distributor node");
        }
    }

    /**
     * Selects a volunteer node that will act as the job distributor
     * @return Node information of the selected node
     */
    private NodeInfo getDistributorNode() {
//        String res = null;
        NodeInfo selectedMember = null;
        TTransport transport = new TSocket("localhost", FrameworkInformation.BOOTSTRAP_PORT);
        try {
            log.info("Getting list of members from bootstrap");
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            Siyapath.Client client = new Siyapath.Client(protocol);
            context.updateMemberSet(CommonUtils.deSerialize(client.getMembers()));
            log.info("Number of members from bootstrapper: " +context.getMemberCount());
            selectedMember = context.getRandomMember();
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

    /**
     *
     * @param node
     */
    private void sendJob(NodeInfo node, String fileName) {
        TTransport transport = new TSocket("localhost", node.getPort());
        Task task = null;

        try {
            task = new Task(123, 234, convertFileToByteBuffer(fileName), "Sending a Temp task data in a String.", "org.test.siyapath.CalcDemo",  CommonUtils.serialize(node), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map taskList = new HashMap<Integer, Task>();
        taskList.put(1, task);
        try {
            log.info("Sending new job to node: " + node.getPort());
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            Siyapath.Client client = new Siyapath.Client(protocol);
            client.submitJob(00001, CommonUtils.serialize(node), taskList);
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

    /**
     *
     * @return ByteBuffer for byte array from given byte-code
     * @throws IOException
     */
    public ByteBuffer convertFileToByteBuffer(String fileName) throws IOException {

        /*temporary location has been set*/
        final String BINARY_FILE_NAME = fileName;
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
