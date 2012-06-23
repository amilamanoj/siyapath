package org.siyapath.job;

/**
 * Created by IntelliJ IDEA.
 * User: Amila Manoj
 * Date: 6/23/12
 * Time: 1:54 AM
 * To change this template use File | Settings | File Templates.
 */
public interface SiyapathJob {

    public void process();
    
    public void setData(Object data);

    public Object getResults();
}
