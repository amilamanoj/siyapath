package org.siyapath.client;

import org.siyapath.NodeInfo;
import org.siyapath.service.Job;

public class JobData {

    private int id;
    private String name;
    private Job job;
    private NodeInfo distributorNode;

    public JobData(int id, String name, Job job, NodeInfo distributorNode) {
        this.id = id;
        this.name = name;
        this.job = job;
        this.distributorNode = distributorNode;
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
}