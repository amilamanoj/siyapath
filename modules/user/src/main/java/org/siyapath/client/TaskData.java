package org.siyapath.client;

import java.io.File;

public class TaskData {
    String name;
    File classFile;
    String inputData;
    String requiredResources;

    public TaskData(String name, File classFile, String inputData, String requiredResources) {
        this.name = name;
        this.classFile = classFile;
        this.inputData = inputData;
        this.requiredResources = requiredResources;
    }

    public String getName() {
        return name;
    }

    public File getClassFile() {
        return classFile;
    }

    public String getInputData() {
        return inputData;
    }

    public String getRequiredResources() {
        return requiredResources;
    }

}