package org.siyapath.monitor;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hyperic.sigar.*;

public final class SigarSystemInformation {

    private static Sigar sigar = new Sigar();

    private SigarSystemInformation(){
    }

    public static String getCPUInformation() throws SigarException {
        CpuInfo[] cpuInfo = sigar.getCpuInfoList();
        return "Cores:" + cpuInfo.length + "-Speed:" + cpuInfo[0].getMhz() + "Mhz";
    }

    public static int getNoOfCores() throws SigarException {
        CpuInfo[] cpuInfo = null;
        cpuInfo = sigar.getCpuInfoList();
        return cpuInfo.length;
    }

    public static int getCPUSpeed() throws SigarException {
        CpuInfo[] cpuInfo = null;
        cpuInfo = sigar.getCpuInfoList();
        return (cpuInfo[0].getMhz() / 1024);
    }

    public static String getMemoryInformation() throws SigarException {
        String memoryStat = "";
        Mem memoryInfo = sigar.getMem();
        memoryStat = "Memory Information";
        memoryStat += "\n" + "Actual total free system memory: " + memoryInfo.getActualFree() / (1024 * 1024) + " MB";
        memoryStat += "\n" + "Actual total used system memory: " + memoryInfo.getActualUsed() / (1024 * 1024) + " MB";
        memoryStat += "\n" + "Total free system memory ......: " + memoryInfo.getFree() / (1024 * 1024) + " MB";
        memoryStat += "\n" + "System Random Access Memory....: " + memoryInfo.getRam() + " MB";
        memoryStat += "\n" + "Total system memory............: " + memoryInfo.getTotal() / (1024 * 1024) + " MB";
        memoryStat += "\n" + "Total used system memory.......: " + memoryInfo.getUsed() / (1024 * 1024) + " MB";
        return memoryStat;
    }


    public static int getFreeMemoryInfo() throws SigarException {
            Mem memoryInfo = sigar.getMem();
            return (int) (memoryInfo.getActualFree() / (1024 * 1024));
    }

    public static void getFileSystemInformation() throws SigarException {
        FileSystem[] filesystem = null;
        filesystem = sigar.getFileSystemList();
        System.out.println("Sigar found " + filesystem.length + " drives");
        for (int i = 0; i < filesystem.length; i++) {
            Map map = filesystem[i].toMap();
            System.out.println("drive " + i + ": " + map);
        }
    }

}

