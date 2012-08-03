package org.siyapath.task;

import java.security.Permission;

/**
 * Created with IntelliJ IDEA.
 * User: Amila Manoj
 * Date: 8/3/12
 * Time: 2:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class SiyapathSecurityManager extends SecurityManager {

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

    public boolean disable(String password){
        if (this.password.equalsIgnoreCase(password)){
            this.active = false;
            return true;
        }
        return false;
    }

    @Override
    public void checkPermission(Permission perm) {
        if (active == true){
            throw new SecurityException();
        }
    }


}
