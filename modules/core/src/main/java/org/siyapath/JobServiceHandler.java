package org.siyapath;

import com.sun.jmx.snmp.tasks.Task;
import org.apache.thrift.TException;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * Date: 2/2/12
 * Time: 8:51 AM
 * To change this template use File | Settings | File Templates.
 */
public class JobServiceHandler implements JobHandlerService.Iface{

    @Override
    public String submitJob(Map<Integer, org.siyapath.Task> tasks, int jobID) throws TException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getJobStatus(int jobID) throws org.apache.thrift.TException{
        return null;
    }

    @Override
    public Map<Integer, org.siyapath.Task> getJobResult(int jobID) throws TException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


}
