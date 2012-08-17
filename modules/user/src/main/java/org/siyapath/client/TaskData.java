package org.siyapath.client;

import java.io.File;

public class TaskData {
    private String name;
    private File classFile;
    private byte[] inputData;
    private String requiredResources;

    public TaskData(String name, File classFile, byte[] inputData, String requiredResources) {
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

    public byte[] getInputData() {
        return inputData;
    }

    public String getRequiredResources() {
        return requiredResources;
    }

}