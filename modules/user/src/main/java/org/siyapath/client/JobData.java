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

package org.siyapath.client;

import org.siyapath.NodeInfo;
import org.siyapath.service.Job;

public class JobData {

    private int id;
    private String name;
    private Job job;
    private NodeInfo distributorNode;
    private NodeInfo backupNode;
    private int distributorFailureCount;
    private boolean isPollingFromBackup;

    public JobData(int id, String name, Job job, NodeInfo distributorNode) {
        this.id = id;
        this.name = name;
        this.job = job;
        this.distributorNode = distributorNode;
        distributorFailureCount = 0;
        isPollingFromBackup = false;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Job getJob() {
        return job;
    }

    public NodeInfo getDistributorNode() {
        return distributorNode;
    }

    public NodeInfo getBackupNode() {
        return backupNode;
    }

    public void setBackupNode(NodeInfo backupNode) {
        this.backupNode = backupNode;
    }

    public int getDistributorFailureCount() {
        return distributorFailureCount;
    }

    public void incrementDistributorFailureCount() {
        this.distributorFailureCount++;
    }

    public boolean isPollingFromBackup() {
        return isPollingFromBackup;
    }

    public void setPollingFromBackup(boolean pollingFromBackup) {
        isPollingFromBackup = pollingFromBackup;
    }
}