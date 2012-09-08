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
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * Creates multiple jobs per client, multiple clients, multiple tasks
 * submits jobs to Siyapath volunteer nodes and keeps polling
 * to know result
 */
public class PerformanceTest {

    private static final Log log = LogFactory.getLog(PerformanceTest.class);
    ConcurrentHashMap<Integer, Long> jobElapsedTimeMap = new ConcurrentHashMap<Integer, Long>();

    public static void main(String[] args) {

        PerformanceTest controller = new PerformanceTest();

        int clients = Integer.parseInt(System.getProperty("clients")); //clients per physical device
        int jobs = Integer.parseInt(System.getProperty("jobs"));       //jobs per client
        int tasks = Integer.parseInt(System.getProperty("tasks"));     //tasks per job

        CountDownLatch endLatch = new CountDownLatch(clients);
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

    /**
     *
     * @param jobs
     * @param tasks
     * @param endLatch
     * @param num
     */
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
    long allJobsStart;
    ArrayList<Job> jobList;
    HashMap<Integer, Long> jobTimeMap;
    CountDownLatch endLatch;
    CountDownLatch jobLatch;

    /**
     *
     * @param jobs
     * @param tasks
     * @param endLatch
     * @param num
     */
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

    /**
     *
     * @param tasks
     * @return
     */
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
                job = userHandler.createJob(taskDataMap, 1);      //default replicas is set to 1
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


        Date allJobsSubmitStart = new Date();             //start submit jobs

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
                } else {
                    allJobsStart = System.currentTimeMillis();
                    jobTimeMap.put(job.getJobID(), allJobsStart);
                }
            } while (submittedJobId < 0);

        }
        Date allJobsSubmitFinish = new Date();                //submitted jobs

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

        Date allJobsFinish = new Date();               //end jobs
        log.info("All jobs completed. Client:" + clientNumber);
        endLatch.countDown();

        log.info("Writing output. Client:" + clientNumber);

        try {
            String fileName = "Client" + clientNumber + "Results.txt";
            PrintWriter writer = new PrintWriter(fileName);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

            for (int id : jobTimeMap.keySet()) {
                writer.println("Job:" + id + "-Time:" + jobTimeMap.get(id) + "ms");
            }
            writer.println("======================================================");
            writer.println("All job start submit: " + sdf.format(allJobsSubmitStart));
            writer.println("All job finish submit: " + sdf.format(allJobsSubmitFinish));
            writer.println("Duration taken to submit: " + (allJobsSubmitFinish.getTime() - allJobsSubmitStart.getTime()) + "ms");
            writer.println("All job complete: " + sdf.format(allJobsFinish));
            writer.println("------------------------------------------------------");
            writer.println("All job duration since start submit: " + (allJobsFinish.getTime() - allJobsSubmitStart.getTime()) + "ms");
            writer.println("All job duration since finish submit: " + (allJobsFinish.getTime() - allJobsSubmitFinish.getTime()) + "ms");
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

    /**
     *
     * @param latch
     * @param handler
     * @param job
     * @param jobTimeMap
     */
    StatusPollThread(CountDownLatch latch, UserHandler handler, Job job, HashMap<Integer, Long> jobTimeMap) {
        this.latch = latch;
        this.handler = handler;
        this.job = job;
        this.jobTimeMap = jobTimeMap;
    }

    @Override
    public void run() {
        Map<Integer, TaskResult> taskCompletionMap = null;
        while (true) {
            try {

                taskCompletionMap = handler.pollStatusFromJobProcessor(job.getJobID());
            } catch (TException e) {    //uh.poll status
                log.error("TException" + e.getMessage());
            }
            if (handler.assessJobStatusFromTaskStatuses(taskCompletionMap)) {
                break;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long jobFinish = System.currentTimeMillis();
        long jobStart = jobTimeMap.get(job.getJobID());
        jobTimeMap.put(job.getJobID(), jobFinish - jobStart);
        log.info("job finished " + job.getJobID());

        latch.countDown();
    }
}



