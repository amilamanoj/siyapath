package org.siyapath.performance.test;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.thrift.TException;
import org.siyapath.client.SubmissionFailedException;
import org.siyapath.client.TaskData;
import org.siyapath.client.UserHandler;
import org.siyapath.service.Job;
import org.siyapath.service.TaskResult;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

public class PerformanceTest {

    private static final Log log = LogFactory.getLog(PerformanceTest.class);
    ConcurrentHashMap<Integer, Long> jobElapsedTimeMap = new ConcurrentHashMap<Integer, Long>();

    public static void main(String[] args) {

        PerformanceTest controller = new PerformanceTest();

        int clients = Integer.parseInt(System.getProperty("clients")); //clients per physical device
        int jobs = Integer.parseInt(System.getProperty("jobs"));       //jobs per client
        int tasks = Integer.parseInt(System.getProperty("tasks"));     //tasks per job

        CountDownLatch endLatch = new CountDownLatch(clients);

//        ImageData imageData = new ImageData();
//        byte[] imageBytes = imageData.getImageData();
//        Map<String, TaskData> taskDataMap = new HashMap<String, TaskData>();
//        File taskFile = new File("./EdgeDetectorTask.class");
//        TaskData taskData = new TaskData("Performance-test Task", taskFile, imageBytes, "medium");

        long allClientStartTime = System.currentTimeMillis();

        for (int i = 0; i < clients; i++) {
            controller.createClients(jobs, tasks, endLatch, i);
        }

        try {
            endLatch.await();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long allClientFinishTime = System.currentTimeMillis();
        log.info("All clients finished. Writing output");
        log.info("Total time consumed : " + (allClientFinishTime - allClientStartTime));

        try {
            String fileName = "ResultsSummery.txt";
            PrintWriter writer = new PrintWriter(fileName);
            writer.println("======================================================");
            writer.println("All clients start: " + allClientStartTime);
            writer.println("All clients finish: " + allClientFinishTime);
            writer.println("All clients duration: " + (allClientFinishTime - allClientStartTime) + "ms");
            writer.println("======================================================");

            writer.flush();
            writer.close();

        } catch (IOException e) {
            log.info("Error while writing " + e.getMessage());
        }


    }

    public void createClients(int jobs, int tasks, CountDownLatch endLatch, int num) {
        ClientThread testThread = new ClientThread(jobs, tasks, endLatch, num);
        testThread.start();
    }

}

class ClientThread extends Thread {

    private static final Log log = LogFactory.getLog(ClientThread.class);

    UserHandler userHandler;
    int jobs, tasks;
    int clientNumber;
    long allJobsStart, jobFinish;                         // get finish time
    ArrayList<Job> jobList;
    HashMap<Integer, Long> jobTimeMap;
    CountDownLatch endLatch;
    CountDownLatch jobLatch;

    ClientThread(int jobs, int tasks, CountDownLatch endLatch, int num) {
        userHandler = new UserHandler();
        this.jobs = jobs;
        this.tasks = tasks;
        this.clientNumber = num;
        jobTimeMap = new HashMap<Integer, Long>();
        jobList = new ArrayList<Job>();
        this.endLatch = endLatch;
        jobLatch = new CountDownLatch(jobs);
    }

    private Map<String, TaskData> createTasks(int tasks) {
        Map<String, TaskData> taskDataMap = new HashMap<String, TaskData>();
        for (int i = 0; i < tasks; i++) {
            ImageData imageData = new ImageData();
            byte[] imageBytes = imageData.getImageData();
            File taskFile = new File("./EdgeDetectorTask.class");
            TaskData taskData = new TaskData("Performance-test Task", taskFile, imageBytes, "medium");
            taskDataMap.put("task" + i + 1, taskData);
        }
        return taskDataMap;
    }

    public void run() {

        log.info("Starting client" + clientNumber);

        Map<String, TaskData> taskDataMap = createTasks(tasks);

        for (int i = 0; i < jobs; i++) {           //tasks for 1 job
            Job job = null;

            try {
                job = userHandler.createJob(taskDataMap, 1);      //todo: default replicas is set to 1
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (job == null) {
                log.fatal("job creation failed");
                System.exit(1);
            }
            jobList.add(job);

        }
        log.info("Client: All jobs created by client:" + clientNumber);


        long allJobsSubmitStart = System.currentTimeMillis();                //start submit jobs

        for (Job job : jobList) {
            int submittedJobId = -1;
            do {

                try {
                    submittedJobId = userHandler.submitJob(job.getJobID() + "", job);
                    jobTimeMap.put(job.getJobID(), System.currentTimeMillis());
                } catch (SubmissionFailedException e) {
                    e.printStackTrace();
                }

                if (submittedJobId < 0) {
                    try {
                        sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {                //if submitted, sets start value on map, else none
                    allJobsStart = System.currentTimeMillis();
                    jobTimeMap.put(job.getJobID(), allJobsStart);
                }
            } while (submittedJobId < 0);    //while exits only after the job was submitted successfully

        }
        long allJobsSubmitFinish = System.currentTimeMillis();                //submitted jobs

        log.info("All jobs submitted by client: " + clientNumber);

        for (Job job : jobList) {
            StatusPollThread pollThread = new StatusPollThread(jobLatch, userHandler, job, jobTimeMap);
            pollThread.start();
        }
        try {
            jobLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long allJobsFinish = System.currentTimeMillis();                //end jobs
        log.info("All jobs completed. Client:" + clientNumber);
        endLatch.countDown();

        log.info("Writing output. Client:" + clientNumber);

        try {
            String fileName = "Client" + clientNumber + "Results.txt";
            PrintWriter writer = new PrintWriter(fileName);

            for (int id : jobTimeMap.keySet()) {
                writer.println("Job:" + id + "-Time:" + jobTimeMap.get(id) + "ms");
            }
            writer.println("======================================================");
            writer.println("All job start submit: " + allJobsSubmitStart);
            writer.println("All job finish submit: " + allJobsSubmitFinish);
            writer.println("Duration taken to submit: " + (allJobsSubmitFinish - allJobsSubmitStart) + "ms");
            writer.println("All job complete: " + allJobsFinish);
            writer.println("------------------------------------------------------");
            writer.println("All job duration since start submit: " + (allJobsFinish - allJobsSubmitStart) + "ms");
            writer.println("All job duration since finish submit: " + (allJobsFinish - allJobsSubmitFinish) + "ms");
            writer.println("======================================================");

            writer.flush();
            writer.close();

        } catch (IOException e) {
            log.info("Error while writing " + e.getMessage());
        }

    }
}

class StatusPollThread extends Thread {

    private static final Log log = LogFactory.getLog(ClientThread.class);

    CountDownLatch latch;
    UserHandler handler;
    Job job;
    HashMap<Integer, Long> jobTimeMap;


    StatusPollThread(CountDownLatch latch, UserHandler handler, Job job, HashMap<Integer, Long> jobTimeMap) {
        this.latch = latch;
        this.handler = handler;
        this.job = job;
        this.jobTimeMap = jobTimeMap;
    }

    @Override
    public void run() {
        Map<Integer, TaskResult> taskCompletionMap = null;
        do {
            try {
                taskCompletionMap = handler.pollStatusFromJobProcessor(job.getJobID());
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (TException e) {
                log.error("Error while polling TException" + e.getMessage());
            }
        } while (!handler.assessJobStatusFromTaskStatuses(taskCompletionMap));
        long jobFinish = System.currentTimeMillis();
        long jobStart = jobTimeMap.get(job.getJobID());
        jobTimeMap.put(job.getJobID(), jobFinish - jobStart);
        log.info("job finished " + job.getJobID());

        latch.countDown();
    }
}


//results are received
//    jobElapsedTimeMap.put(job.getJobID(),(jobFinish-jobStart));

//    log.info("Finishing job : "+job.getJobID());

//    log.info("Thread run count : "+endLatch.getCount());


