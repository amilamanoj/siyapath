package org.siyapath;

import java.net.InetSocketAddress;
import java.util.ArrayList;

/**
 * Date: 2/1/12
 * Time: 5:21 PM
 */
public class FrameworkInformation {

    public static final String BOOTSTRAPPER1 = "10.1.0.1";
    public static final String BOOTSTRAPPER2 = "10.1.0.2";
    public static final String BOOTSTRAPPER3 = "10.1.0.3";
    public static final int PORT = 9090;

    public static ArrayList<InetSocketAddress> getKnownBootstrappers(){
        ArrayList<InetSocketAddress> knownBootstrappers = new ArrayList<InetSocketAddress>();
        knownBootstrappers.add(new InetSocketAddress(BOOTSTRAPPER1, PORT));
        knownBootstrappers.add(new InetSocketAddress(BOOTSTRAPPER2, PORT));
        knownBootstrappers.add(new InetSocketAddress(BOOTSTRAPPER3, PORT));
        return knownBootstrappers;
    }
}
