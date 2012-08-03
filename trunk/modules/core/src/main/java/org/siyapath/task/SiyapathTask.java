package org.siyapath.task;

/**
 *
 */
public interface SiyapathTask {

    void process();
    
    void setData(Object data);

    Object getResults();
}
