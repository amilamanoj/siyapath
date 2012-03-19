package org.siyapath.monitor;

import java.util.Map;

import org.hyperic.sigar.*;

public class SigarSystemInformation {

    private static Sigar sigar = new Sigar();

    public static void main(String[] args) {
        getCPUInformation();
        getMemoryInformation();
        getFileSystemInformation();
    }

    public static void getCPUInformation() {
        CpuInfo[] cpuinfo = null;
        try {
            cpuinfo = sigar.getCpuInfoList();
        } catch (SigarException se) {
            se.printStackTrace();
        }
        System.out.println("Sigar found " + cpuinfo.length + " CPU(s)");

        for (int i = 0; i < cpuinfo.length; i++) {
            Map map = cpuinfo[i].toMap();
            System.out.println("CPU " + i + ": " + map);
        }
    }

    public static void getMemoryInformation() {

        Mem mem = null;
        try {
            mem = sigar.getMem();
        } catch (SigarException se) {
            se.printStackTrace();
        }

        Map map = mem.toMap();
//        System.out.println(map);
        System.out.println("Memory Information");
        System.out.println("Actual total free system memory: " + mem.getActualFree() / (1024 * 1024) + " MB");
        System.out.println("Actual total used system memory: " + mem.getActualUsed() / (1024 * 1024) + " MB");
        System.out.println("Total free system memory ......: " + mem.getFree() / (1024 * 1024) + " MB");
        System.out.println("System Random Access Memory....: " + mem.getRam() + " MB");
        System.out.println("Total system memory............: " + mem.getTotal() / (1024 * 1024) + " MB");
        System.out.println("Total used system memory.......: " + mem.getUsed() / (1024 * 1024) + " MB");

    }

    public static void getFileSystemInformation() {

        FileSystem[] filesystem = null;
        try {
            filesystem = sigar.getFileSystemList();
        } catch (SigarException se) {
            se.printStackTrace();
        }
        System.out.println("Sigar found " + filesystem.length + " drives");

        for (int i = 0; i < filesystem.length; i++) {
            Map map = filesystem[i].toMap();
            System.out.println("drive " + i + ": " + map);
        }
    }

}

