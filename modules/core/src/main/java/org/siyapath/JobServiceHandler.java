package org.siyapath;

import org.apache.thrift.TException;

import java.util.Map;

public class JobServiceHandler implements JobHandlerService.Iface{

    @Override
    public String submitJob(Map<Integer, org.siyapath.Task> tasks, int jobID) throws TException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getJobStatus(int jobID) throws org.apache.thrift.TException{
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<Integer, org.siyapath.Task> getJobResult(int jobID) throws TException {
        throw new UnsupportedOperationException();
    }


}
