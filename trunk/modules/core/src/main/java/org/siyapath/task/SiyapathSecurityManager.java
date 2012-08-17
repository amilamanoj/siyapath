package org.siyapath.task;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.siyapath.PeerListener;

import java.security.Permission;

/**
 * Created with IntelliJ IDEA.
 * User: Amila Manoj
 * Date: 8/3/12
 * Time: 2:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class SiyapathSecurityManager extends SecurityManager {
    private static final Log log = LogFactory.getLog(SiyapathSecurityManager.class);

    private String password;
    private boolean active;

    public SiyapathSecurityManager(String password) {
        this.password = password;
        this.active = true;
    }


//    @Override

    /**
     * Does not permit the code to stop JVM
     */
//    public void checkExit(int status) {
//        throw new SecurityException();
//    }
    //TODO: override other checkXXX methods
    // the manager denies everything by default
    public boolean disable(String password) {
        if (this.password.equalsIgnoreCase(password)) {
            this.active = false;
            return true;
        }
        return false;
    }

    @Override
    public void checkPermission(Permission perm) {
        if (active == true) {
            String threadName = Thread.currentThread().getName();
            if (threadName.startsWith("task-thread")) {
                log.debug("Blocking an operation for task thread");
                throw new SecurityException();
            } else {
//                log.debug("Allowing an operation for non task thread");
            }
        }
    }

//    @Override
//    public void checkAccept(String host, int port) {
//        log.debug("allowing Accept");
//    }
//
//    @Override
//    public void checkPropertiesAccess() {
//        log.debug("allowing PropertiesAccess");
//    }
//
//    @Override
//    public void checkPropertyAccess(String key) {
//        log.debug("allowing PropertiesAccess");
//
//    }
//
//    @Override
//    public void checkListen(int port) {
//        log.debug("allowing Listen");
//    }
//
//    @Override
//    public void checkConnect(String host, int port) {
//        log.debug("allowing Connect for: " +  Thread.currentThread().getName());
//    }
//
//    @Override
//    public void checkConnect(String host, int port, Object context) {
//        log.debug("allowing Connect for: " +  Thread.currentThread().getName());
//    }
}
