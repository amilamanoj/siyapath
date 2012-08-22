package org.siyapath.performance.test;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.thrift.TException;
import org.siyapath.client.SubmissionFailedException;
import org.siyapath.client.TaskData;
import org.siyapath.client.UserHandler;
import org.siyapath.service.Job;
import org.siyapath.service.TaskResult;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SiyapathClientController {

    private static final Log log = LogFactory.getLog(SiyapathClientController.class);

    public static void main(String[] args) {

        SiyapathClientController controller = new SiyapathClientController();
        int clients = Integer.parseInt(System.getProperty("clients")); //clients per physical device
        int jobs = Integer.parseInt(System.getProperty("jobs"));       //jobs per client
        int tasks = Integer.parseInt(System.getProperty("tasks"));     //tasks per job


        ImageData imageData = new ImageData();
        byte[] imageBytes = imageData.getImageData();

        controller.createClients(clients, jobs, tasks, imageBytes);
    }

    public void createClients(int clients, int jobs, int tasks, byte[] imageBytes){

        for (int i=0; i<clients; i++){
            ClientThread clientThread = new ClientThread(jobs, tasks, imageBytes);
            clientThread.start();
        }
    }

    class ClientThread extends Thread{

        long startTime;
        long endTime;
        int jobsPerClient, tasksPerJob;
        UserHandler userHandler;
        ArrayList<Job> jobs;
        byte[] imageBytes;

        ClientThread (int jobsPerClient, int tasksPerJob, byte[] imageBytes){
            this.jobsPerClient = jobsPerClient;
            this.tasksPerJob = tasksPerJob;
            this.imageBytes = imageBytes;
            userHandler = new UserHandler();
            jobs = new ArrayList<Job>(jobsPerClient);
        }

        public void run(){

            try {
                //create multiple jobs of one client
                makeJobs(imageBytes);
                /**
                 * multiple jobs of one client submitted sequentially
                 * requires multiple threads if more jobs are submitted
                 */
            startTime = System.nanoTime();
                for (Job job : jobs){
                    userHandler.submitJob(job.getJobID()+"", job);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SubmissionFailedException e) {
                e.printStackTrace();
            }

            if(!jobs.isEmpty()){
                /**
                 * sequential polling assuming user submits 1 or min jobs at a time,
                 * requires multiple threads if more jobs are submitted
                 * Will exit only when all tasks of all jobs are marked DONE.
                 * Flow to be decided.
                 */
                for(Job job : jobs){
                    Map<Integer, TaskResult> taskCompletionMap;
                    try{
                        do{
                            taskCompletionMap = userHandler.pollStatusFromJobProcessor(job.getJobID());
                        }while (!userHandler.assessJobStatusFromTaskStatuses(taskCompletionMap));
                    }catch (TException e){
                        e.printStackTrace();
                    }
                }

                /**
                 * Get results after all jobs are complete
                 * Flow to be decided
                 * get result service method tbd
                 */
//                for (Job job : jobs){
//                    try {
//                        userHandler.getJobResults(job.getJobID());
//                        log.info("Results retrieved from Job Processor for job:" + job.getJobID());
//                    } catch (TException e) {
//                        e.printStackTrace();
//                    }
//                }
            }
            endTime = System.nanoTime();
            log.info("Total elapsed time for " + jobsPerClient + " jobs with " + tasksPerJob + " tasks: "
                    + (endTime-startTime) + "ns");

        }

        /**
         * creates one job with system property defined number of tasks
         */
        public void makeJobs(byte[] imageBytes) throws IOException {
        
            Map<String, TaskData> taskDataMap = new HashMap<String, TaskData>();

            File taskFile = new File("modules/integration/EdgeDetectorTask.class");
            TaskData taskData = new TaskData("Performance-test Task", taskFile, imageBytes, "Cores:4-Speed:2267Mhz");


            for (int i=0; i<jobsPerClient; i++){
                for (int j=0; j<tasksPerJob; j++){
                    taskDataMap.put("Task" + j+1, taskData);
                }

                try {
                    Job job = userHandler.createJob(taskDataMap);
                    jobs.add(job);
                    log.info("Created Job:" + job.getJobID() + " with " + tasksPerJob + " tasks");
//                return job;
                } catch (IOException e) {
                    e.printStackTrace();
//                throw new IOException(e.getMessage());
                }
            }
        }






    }
}
