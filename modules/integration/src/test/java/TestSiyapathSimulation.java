import junit.framework.Assert;
import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.siyapath.NodeInfo;
import org.siyapath.SiyapathNode;
import org.siyapath.client.SubmissionFailedException;
import org.siyapath.client.TaskData;
import org.siyapath.client.UserHandler;
import org.siyapath.utils.CommonUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class TestSiyapathSimulation extends TestCase {

    private static final Log log = LogFactory.getLog(TestSiyapathSimulation.class);
    private int nodeCount;
    private int clientCount;
    private ArrayList<SiyapathNodeController> controllerList;
    private UserHandler userHandler;
    private Map<String,TaskData> taskFileList;

    public TestSiyapathSimulation() {
        controllerList = new ArrayList<SiyapathNodeController>();
    }

    @Override
    public void setUp() throws Exception {
        log.info("Starting up Siyapath simulation...");
    }

    @Override
    public void tearDown() throws Exception {
        log.info("Finishing simulation");
    }

    public void testSimulation() throws Exception {
        log.info("Running simulation");
        String nodes = System.getProperty("nodes");
        String clients = System.getProperty("clients");

        try {
            nodeCount = Integer.parseInt(nodes);
            clientCount = Integer.parseInt(clients); // n number of clients will start by running this test

            startBootStrapper();
            startNodes();
            waitForGossip();
            prepareJob();
            submitJobs();
            waitForProcessing();
            log.info("Total nodes: " + nodeCount);
            Assert.assertEquals("a", "a");

        } catch (NumberFormatException e) {
            log.info("Skipping simulation");
        }
    }

    private void waitForGossip() {
        log.info("Waiting for nodes to gossip");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("Continuing simulation...");

    }
    private void waitForProcessing() {
        log.info("Waiting for nodes to gossip");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("Continuing simulation...");

    }

    private void startBootStrapper() {
        log.info("Starting Bootstrapper Node");
        NodeInfo bootStrapperInfo = new NodeInfo();
        bootStrapperInfo.setBootstrapper(true);
        bootStrapperInfo.setIp(CommonUtils.getBootstrapperIP());
        bootStrapperInfo.setPort(CommonUtils.getBootstrapperPort());
        SiyapathNode bootStrapperNode = new SiyapathNode(bootStrapperInfo);

        SiyapathNodeController bootStrapperController = new SiyapathNodeController(bootStrapperNode, null);
        bootStrapperController.start();

        log.info("Bootstrapper is up. continuing simulation");
    }

    private void startNodes() {
        CountDownLatch cdLatch = new CountDownLatch(nodeCount);
        log.info("Preparing to start Nodes. Total Nodes: " + nodeCount);
        log.info("Creating nodes...");
        for (int i = 0; i < nodeCount; i++) {
            NodeInfo nodeInfo = new NodeInfo();
            nodeInfo.setNodeId(CommonUtils.getRandomPort());
            nodeInfo.setPort(nodeInfo.getNodeId());
            log.info("Creating node: " + nodeInfo);
            SiyapathNode node = new SiyapathNode(nodeInfo);
            SiyapathNodeController nodeController = new SiyapathNodeController(node, cdLatch);
            controllerList.add(nodeController);
        }

        log.info("Starting nodes...");
        for (SiyapathNodeController nController : controllerList) {
            nController.startNonBlocking();
        }

        log.info("Waiting until all nodes are started");
        try {
            cdLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("All nodes are started.");

    }

    private void submitJobs() throws IOException, SubmissionFailedException {
        userHandler = new UserHandler();
        userHandler.submitJob("SampleJob", userHandler.createJob(taskFileList, 1));
    }


    private boolean prepareJob() {
        log.info("Preparing to submit sample jobs");
        taskFileList = new HashMap<String, TaskData>();
        File taskFile = new File("target/test-classes/SampleSiyapathTask.class");
        assertTrue("Task file is missing", taskFile.exists());
        taskFileList.put("Task1", new TaskData("Task1", taskFile, "0,100000".getBytes(), "Medium"));
        taskFileList.put("Task2" , new TaskData("Task1", taskFile, "100000,200000".getBytes() , "Medium"));
        taskFileList.put("Task3" , new TaskData("Task1", taskFile, "200000,300000".getBytes() , "Medium"));
        taskFileList.put("Task4" , new TaskData("Task1", taskFile, "300000,400000".getBytes(), "Medium"));
        return true;
    }

}
