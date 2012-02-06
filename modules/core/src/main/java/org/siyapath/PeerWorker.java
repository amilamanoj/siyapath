package org.siyapath;

import sun.font.TrueTypeFont;

/**
 * Created by IntelliJ IDEA.
 * User: amila
 * Date: 2/6/12
 * Time: 9:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class PeerWorker {

    public void work(){
          new WorkerThread().start();
    }
    
    private class WorkerThread extends Thread {

        public boolean isRunning = false;
        
        @Override
        public void run() {
            
            isRunning = true;

            while (isRunning){
                
            }
        }
    }
}
