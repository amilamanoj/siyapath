package org.siyapath.monitor;

import org.hyperic.sigar.*;

/**
 * Monitor the System and resources
 */

public final class SigarSystemInformation {

    private static Sigar sigar = new Sigar();

    private SigarSystemInformation(){
    }

    /**
     * Get System CPU info
     *
     * @return CPU Info
     * @throws SigarException
     */
    public static String getCPUInformation() throws SigarException {
        CpuInfo[] cpuInfo = sigar.getCpuInfoList();
        return "Cores:" + cpuInfo.length + "-Speed:" + cpuInfo[0].getMhz() + "Mhz";
    }

    /**
     * Get no of Cores
     * @return no of cores
     * @throws SigarException
     */
    public static int getNoOfCores() throws SigarException {
        CpuInfo[] cpuInfo = null;
        cpuInfo = sigar.getCpuInfoList();
        return cpuInfo.length;
    }

    /**
     * Get system CPU speed
     *
     * @return CPU Speed
     * @throws SigarException
     */
    public static int getCPUSpeed() throws SigarException {
        CpuInfo[] cpuInfo = null;
        cpuInfo = sigar.getCpuInfoList();
        return (cpuInfo[0].getMhz() / 1024);
    }

    /**
     * Get full system memory info
     * @return
     * @throws SigarException
     */
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

    /**
     * Get system free memory
     *
     * @return system free memory
     * @throws SigarException
     */
    public static int getFreeMemoryInfo() throws SigarException {
            Mem memoryInfo = sigar.getMem();
            return (int) (memoryInfo.getActualFree() / (1024 * 1024));
    }

}

