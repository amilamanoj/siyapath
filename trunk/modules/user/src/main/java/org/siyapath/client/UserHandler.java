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
import org.siyapath.Siyapath;
import org.siyapath.Task;

import javax.swing.*;
import java.io.*;
import java.net.ConnectException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class UserHandler {
    private static final Log log = LogFactory.getLog(UserHandler.class);

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
        String res = null;
        TTransport transport = new TSocket("localhost", FrameworkInformation.BOOTSTRAP_PORT);
        try {
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            Siyapath.Client client = new Siyapath.Client(protocol);
            log.info("Getting list of members from bootstrap");
            Object memberArray[] =client.getMembers().toArray();
            log.info("Number of members: "+memberArray.length);
            Integer selectedMember = (Integer) memberArray[new Random().nextInt(memberArray.length)];
            Task task = new Task("123","234",convertFileToByteBuffer(fileName),"Sending a Temp task data in a String.","org.test.siyapath.CalcDemo");
            Map job = new HashMap<Integer, Task>();
            job.put(1,task);
            log.info("Sending new job to node: "+selectedMember);
            client.submitJob(job, 001);
        } catch (TTransportException e) {
            if (e.getCause() instanceof ConnectException) {
                res = "connecEx";
                e.printStackTrace();
            }
        } catch (TException e) {
            res = "tEx";
            e.printStackTrace();
        } catch (IOException e) {
            log.warn("Could not create a task out of given parameters.");
            e.printStackTrace();
        } finally {
            transport.close();
        }
    }

    //TODO: this method is replicated. move to common utils
    public ByteBuffer convertFileToByteBuffer(String fileLocation) throws IOException {
        File file = new File(fileLocation);
        InputStream inputStream = null;

        byte[] bytes = new byte[(int)file.length()];
        if (file.length() > Integer.MAX_VALUE) {
            log.error("File is too large.");
        }
        try{
            inputStream = new BufferedInputStream(new FileInputStream(file));
            int offset=0, numRead;

            while (offset < bytes.length
                    && (numRead=inputStream.read(bytes, offset, bytes.length-offset)) >= 0) {
                offset += numRead;
            }
            // Ensure all the bytes have been read
            if (offset < bytes.length) {
                log.warn("Could not completely read file " + file.getName());
                throw new IOException("Could not completely read file " + file.getName());
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
