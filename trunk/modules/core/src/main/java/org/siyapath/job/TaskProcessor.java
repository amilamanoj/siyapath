package org.siyapath.job;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.siyapath.Task;

import java.lang.reflect.InvocationTargetException;

/*
* Required implementation for the submit task operation on IDL, at the recipient's end.
* Assumes that a single .class is sent by the TaskDistributor node ftm.
* TODO: extend to use a .zip/jar with multiple .class files
* */
public class TaskProcessor {

    private final Log log = LogFactory.getLog(TaskProcessor.class);
    private Task task;
    private String className=null;
    private Class theLoadedClass;
    TaskClassLoader taskClassLoader;

    public TaskProcessor(Task task, String className){
        this.className = className;
        this.task = task;
//        to be used for jar
//        for(String name : names){}
    }

    public void processTask(){
        taskClassLoader = new TaskClassLoader();
        try {
            theLoadedClass = taskClassLoader.loadClassToProcess(task.getTaskProgram(), className);
            Object instanceForTesting = theLoadedClass.newInstance();
            log.info("Task processing begins.");
            //currently uses the org.siyapath.sample.CalcDemo.processSampleJob() method
            theLoadedClass.getMethod("processSampleJob",null).invoke(instanceForTesting);
            log.info("Task processing is completed.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
