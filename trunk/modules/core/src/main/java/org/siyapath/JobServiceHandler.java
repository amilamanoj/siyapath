package org.siyapath;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * Date: 2/2/12
 * Time: 8:51 AM
 * To change this template use File | Settings | File Templates.
 */
public class JobServiceHandler implements JobHandlerService.Iface{

    public String submitJob(Map<Integer,Task> tasks, int jobID) throws org.apache.thrift.TException{
        return null;
    }

    public String getJobStatus(int jobID) throws org.apache.thrift.TException{
        return null;
    }

    public Map<Integer,Task> getJobResult(int jobID) throws org.apache.thrift.TException{
        return null;
    }
}
