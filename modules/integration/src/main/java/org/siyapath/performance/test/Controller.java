package org.siyapath.performance.test;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.thrift.TException;
import org.siyapath.client.SubmissionFailedException;
import org.siyapath.client.TaskData;
import org.siyapath.client.UserHandler;
import org.siyapath.service.Job;
import org.siyapath.service.TaskResult;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

public class Controller {

    private static final Log log = LogFactory.getLog(Controller.class);
    ConcurrentHashMap<Integer, Long> jobElapsedTimeMap = new ConcurrentHashMap<Integer, Long>();

    public static void main(String[] args) {

        Controller controller = new Controller();


        int clients = Integer.parseInt(System.getProperty("clients")); //clients per physical device
        int jobs = Integer.parseInt(System.getProperty("jobs"));       //jobs per client
        int tasks = Integer.parseInt(System.getProperty("tasks"));     //tasks per job

        CountDownLatch endLatch = new CountDownLatch(clients * jobs);

//        ImageData imageData = new ImageData();
//        byte[] imageBytes = imageData.getImageData();
//        Map<String, TaskData> taskDataMap = new HashMap<String, TaskData>();
//        File taskFile = new File("./EdgeDetectorTask.class");
//        TaskData taskData = new TaskData("Performance-test Task", taskFile, imageBytes, "medium");

        long allJobStartTime = System.currentTimeMillis();

        for (int i = 0; i < clients; i++) {
            for (int j = 0; j < jobs; j++) {
                controller.createClientJobs(tasks, endLatch);
            }
        }

        try {
            endLatch.await();
            long allJobFinishTime = System.currentTimeMillis();
            log.info("Writing to disk");
            controller.printTimes();
            log.info("Total time consumed : " + (allJobFinishTime - allJobStartTime));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void printTimes() {
        log.info("All threads finished!");
//        for (Map.Entry<Integer,Long> elapsedTime : jobElapsedTimeMap.entrySet()){
//            log.info(elapsedTime.getKey() + " for job: " + elapsedTime.getValue());
//        }

        String newLine = System.getProperty("line.separator");
        String printLine = "";
        log.info("elapsedtimeMap size" + jobElapsedTimeMap.size());
        for (Map.Entry<Integer, Long> eachValue : jobElapsedTimeMap.entrySet()) {
            printLine = printLine + "Job id: " + eachValue.getKey() + " : time = " + eachValue.getValue() + newLine;
            log.info("id, time" + eachValue.getKey() + ", " + eachValue.getValue());
        }

        try {

            File file = new File("TestResults.txt");

            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fileWriter = new FileWriter(file.getName(), true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(printLine);
            bufferedWriter.flush();
            bufferedWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void createClientJobs(int tasksPerJob, CountDownLatch endLatch) {

        TestThread testThread = new TestThread(tasksPerJob, endLatch);
        testThread.start();
    }

    class TestThread extends Thread {

        UserHandler userHandler;
        int tasksPerJob;
        long jobStart, jobFinish;                         // get finish time
        //        HashMap<Integer, Long> jobTimeMap;
        CountDownLatch endLatch;

        TestThread(int tasksPerJob, CountDownLatch endLatch) {
            userHandler = new UserHandler();
            this.tasksPerJob = tasksPerJob;
//            jobTimeMap = new HashMap<Integer, Long>();
            this.endLatch = endLatch;
        }

        public void run() {

            Map<String, TaskData> taskDataMap = new HashMap<String, TaskData>();

            ImageData imageData = new ImageData();
            byte[] imageBytes = imageData.getImageData();
            File taskFile = new File("./EdgeDetectorTask.class");
            TaskData taskData = new TaskData("Performance-test Task", taskFile, imageBytes, "medium");

            for (int i = 0; i < tasksPerJob; i++) {           //tasks for 1 job
                taskDataMap.put("Task" + i + 1, taskData);
            }

            Job job = null;
            try {
                job = userHandler.createJob(taskDataMap, 1);  //todo: default replicas is set to 1
                if (job != null) {


//                    if(userHandler.submitJob(job.getJobID()+"", job)<0){    //if not submitted
//
//                    }

                    int submittedJobId = -1;
                    do {
                        try {
                            jobStart = System.currentTimeMillis();                //start job
                            submittedJobId = userHandler.submitJob(job.getJobID() + "", job);
                        } catch (SubmissionFailedException e) {
                            e.printStackTrace();
                        }

                        if (submittedJobId < 0) {
                            try {
                                sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else {                //if submitted, sets start value on map, else none
//                            jobStart = System.currentTimeMillis();
//                            jobTimeMap.put(job.getJobID(), jobStart);
                        }
                    } while (submittedJobId < 0);    //while exits only after the job was submitted successfully


                } else {
                    log.info("Job was null");
                }
            } catch (IOException e) {   //uh.createJob()
                log.error("IOException" + e.getMessage());
            }


            Map<Integer, TaskResult> taskCompletionMap = null;
            do {
                try {

                    taskCompletionMap = userHandler.pollStatusFromJobProcessor(job.getJobID());
                } catch (TException e) {    //uh.poll status
                    log.error("TException" + e.getMessage());
                }

            } while (!userHandler.assessJobStatusFromTaskStatuses(taskCompletionMap));

            log.info("job finished. updating map " + job.getJobID());
            //results are received
            jobFinish = System.currentTimeMillis();
            jobElapsedTimeMap.put(job.getJobID(), (jobFinish - jobStart));

            log.info("Finishing job : " + job.getJobID());

            endLatch.countDown();
            log.info("Thread run count : " + endLatch.getCount());

        }

    }

}
