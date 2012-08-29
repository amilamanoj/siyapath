package org.siyapath.task;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Is responsible for loading classes given the bytes that make up the class
 * data sent by user
 */
public class TaskClassLoader extends ClassLoader{

    private final Log log = LogFactory.getLog(TaskClassLoader.class);

    /**
     *
     * @param byteArray The bytes that make up the class data.
     * @param name class name
     * @return Class instance specified by byteArray and binary name of the class sent
     * @throws ClassNotFoundException
     */
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
