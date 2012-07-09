package org.siyapath.job;

/**
 *
 */
public interface SiyapathTask {

    public void process();
    
    public void setData(Object data);

    public Object getResults();
}
