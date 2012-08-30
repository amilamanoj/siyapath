package org.siyapath.task;

/**
 *
 */
public interface SiyapathTask {

    void process();
    
    void setData(byte[] data);

    void setMetaData(String data);

    byte[] getResults();

}
