/*
 * Distributed under the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

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
     * @return String representation of memory information
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

