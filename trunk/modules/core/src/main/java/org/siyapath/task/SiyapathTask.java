package org.siyapath.task;

/**
 *
 */
public interface SiyapathTask {

    void process();
    
    void setData(byte[] data);

    byte[] getResults();
}
