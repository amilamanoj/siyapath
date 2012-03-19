package org.siyapath.monitor;

import java.io.File;

/*
 * very limited system information from the Runtime class
 */
public class LimitedSystemInformation {

    public static void main(String[] args) {
        LimitedSystemInformation monitor = new LimitedSystemInformation();
        monitor.getRuntimeSysInfo();
    }

    public void getRuntimeSysInfo(){
        processorInformation();
        memoryInformation();
        fileSystemInformation();
    }

    private void processorInformation() {
        System.out.println("================processor================" );
        /* Total number of processors or cores available to the JVM */
        System.out.println("Available processors (cores): " +
                Runtime.getRuntime().availableProcessors());
    }

    private void memoryInformation() {
        System.out.println("==================memory=================" );

        /* Total amount of free memory available to the JVM */
        System.out.println("Free memory (Mbytes): " +
                Runtime.getRuntime().freeMemory()/(1024*1024));

        /* This will return Long.MAX_VALUE if there is no preset limit */
        long maxMemory = Runtime.getRuntime().maxMemory();
        /* Maximum amount of memory the JVM will attempt to use */
        System.out.println("Maximum memory (Mbytes): " +
                (maxMemory == Long.MAX_VALUE ? "no limit" : maxMemory/(1024*1024)));

        /* Total memory currently in use by the JVM */
        System.out.println("Total memory (Mbytes): " +
                Runtime.getRuntime().totalMemory()/(1024*1024));
    }

    private void fileSystemInformation() {
        System.out.println("================filesystem===============" );
        /* Get a list of all filesystem roots on this system */
        File[] roots = File.listRoots();

        /* For each filesystem root, print some info */
        for (File root : roots) {
            System.out.println("File system root: " + root.getAbsolutePath());
            System.out.println("Total space (Mbytes): " + root.getTotalSpace()/(1024*1024));
            System.out.println("Free space (Mbytes): " + root.getFreeSpace()/(1024*1024));
            System.out.println("Usable space (Mbytes): " + root.getUsableSpace()/(1024*1024));
        }
    }

}
