package org.siyapath.monitor;

import java.util.Map;

import org.hyperic.sigar.*;

public class SigarSystemInformation {

    private static Sigar sigar = new Sigar();

    public static String getCPUInformation() {
        CpuInfo[] cpuInfo = null;
        try {
            cpuInfo = sigar.getCpuInfoList();

        } catch (SigarException se) {
            se.printStackTrace();
        }
        return "Cores:" + cpuInfo.length + "-Speed:" + cpuInfo[0].getMhz() + "Mhz";
    }


    public static String getCPUInformationD() {
        CpuInfo[] cpuInfo = null;
        String cpuStat = "";
        try {
            cpuInfo = sigar.getCpuInfoList();
        } catch (SigarException se) {
            se.printStackTrace();
        }
        cpuStat = "CPU Information";
        cpuStat += "Found " + cpuInfo.length + " CPU(s)";

        for (int i = 0; i < cpuInfo.length; i++) {
            Map map = cpuInfo[i].toMap();
            cpuStat += "\n" + "CPU " + i + ": " + map;
        }

        return cpuStat;
    }


    public static String getMemoryInformation() {

        Mem memoryInfo = null;
        String memoryStat = "";
        try {
            memoryInfo = sigar.getMem();
        } catch (SigarException se) {
            se.printStackTrace();
        }

        memoryStat = "Memory Information";
        memoryStat += "\n" + "Actual total free system memory: " + memoryInfo.getActualFree() / (1024 * 1024) + " MB";
        memoryStat += "\n" + "Actual total used system memory: " + memoryInfo.getActualUsed() / (1024 * 1024) + " MB";
        memoryStat += "\n" + "Total free system memory ......: " + memoryInfo.getFree() / (1024 * 1024) + " MB";
        memoryStat += "\n" + "System Random Access Memory....: " + memoryInfo.getRam() + " MB";
        memoryStat += "\n" + "Total system memory............: " + memoryInfo.getTotal() / (1024 * 1024) + " MB";
        memoryStat += "\n" + "Total used system memory.......: " + memoryInfo.getUsed() / (1024 * 1024) + " MB";

        return memoryStat;
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

