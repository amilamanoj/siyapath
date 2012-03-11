package org.siyapath.job;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TaskClassLoader extends ClassLoader{

    private final Log log = LogFactory.getLog(TaskClassLoader.class);

    public Class loadClassToProcess(byte[] byteArray, String name) throws ClassNotFoundException {

        Class loadingClass = defineClass(name, byteArray, 0, byteArray.length);
        if(loadingClass == null){
            throw new ClassNotFoundException("Error in loading " + name);
        }
        resolveClass(loadingClass);
        log.info("Successfully loaded class " + name);

    return loadingClass;
    }
}
