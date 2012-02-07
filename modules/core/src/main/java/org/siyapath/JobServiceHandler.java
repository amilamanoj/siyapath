package org.siyapath;

import org.apache.thrift.TException;

import java.util.Map;

public class JobServiceHandler implements JobHandlerService.Iface{

    @Override
    public String submitJob(Map<Integer, org.siyapath.Task> tasks, int jobID) throws TException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getJobStatus(int jobID) throws org.apache.thrift.TException{
        return null;
    }

    @Override
    public Map<Integer, org.siyapath.Task> getJobResult(int jobID) throws TException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


}
