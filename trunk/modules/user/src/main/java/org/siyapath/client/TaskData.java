package org.siyapath.client;

import java.io.File;

public class TaskData {
    private String name;
    private File classFile;
    private byte[] inputData;
    private String requiredResourceLevel;

    public TaskData(String name, File classFile, byte[] inputData, String requiredResourceLevel) {
        this.name = name;
        this.classFile = classFile;
        this.inputData = inputData;
        this.requiredResourceLevel = requiredResourceLevel;
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

    public String getRequiredResourceLevel() {
        return requiredResourceLevel;
    }

}