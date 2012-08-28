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
        if (active) {
            String threadName = Thread.currentThread().getName();
            if (threadName.startsWith("task-thread")) {
                log.debug("Blocking an operation for task thread");
                throw new SecurityException();
//            } else {
//                log.debug("Allowing an operation for non task thread");
            }
        }
    }

//    /**
//     * Does not permit the code to stop JVM
//     */
//    @Override
//    public void checkExit(int status) {
//        throw new SecurityException();
//    }

//    @Override
//    public void checkLink(String lib) {
//        log.debug("allowing link");
//    }
//
//    @Override
//    public void checkRead(String file) {
//        log.debug("allowing read");
//    }
//
//    @Override
//    public void checkAccess(ThreadGroup g) {
//        log.debug("allowing thread access");
//    }
//
//    @Override
//    public void checkAccess(Thread t) {
//        log.debug("allowing thread access");
//    }
//
//    @Override
//    public void checkMemberAccess(Class<?> clazz, int which) {
//        log.debug("allowing member access");
//    }
//
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
