package org.siyapath.client;

import java.io.File;

public class TaskData {
        String name;
        File classFile;
        String inputData;

    public TaskData(String name, File classFile, String inputData) {
        this.name = name;
        this.classFile = classFile;
        this.inputData = inputData;
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
}