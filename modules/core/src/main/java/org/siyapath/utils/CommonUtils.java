package org.siyapath.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: amila
 * Date: 2/1/12
 * Time: 11:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class CommonUtils {
    
    private static Log log = LogFactory.getLog(CommonUtils.class);

    //temporary testing purposes
    public static InetSocketAddress getRandomAddress(){
        int port = 9090;
        String host="10.1.";
        host = host + new Random().nextInt(255) + ".";
        host = host + new Random().nextInt(255) ;
        InetSocketAddress address = new InetSocketAddress(host, port);
        return address;
    }

    public static String getIPAddress() throws Exception {
        List<InetAddress> ipAddresses = new ArrayList<InetAddress>();
        String ipAddress = null;

        Enumeration e = NetworkInterface.getNetworkInterfaces();
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
