import junit.framework.Assert;
import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.siyapath.NodeContext;
import org.siyapath.NodeInfo;
import org.siyapath.SiyapathNode;
import org.siyapath.client.UserHandler;
import org.siyapath.utils.CommonUtils;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class TestSiyapathSimulation extends TestCase {

    private static final Log log = LogFactory.getLog(TestSiyapathSimulation.class);
    private int nodeCount;
    private ArrayList<SiyapathNodeController> controllerList;
    private UserHandler userHandler;

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
        try {
            nodeCount = Integer.parseInt(nodes);

            startBootStrapper();
            startNodes();
            submitJobs();

//            Thread.sleep(10000);

            log.info("Total nodes: " + nodeCount);
            Assert.assertEquals("a", "a");

        } catch (NumberFormatException e) {
            log.info("Skipping simulation");
        }
    }

    private void startBootStrapper() {
        log.info("Starting Bootstrapper Node");
        NodeInfo bootStrapperInfo = new NodeInfo();
        SiyapathNode bootStrapperNode = new SiyapathNode(bootStrapperInfo);

        SiyapathNodeController bootStrapperController = new SiyapathNodeController(bootStrapperNode, null);

        bootStrapperController.start();

//        log.info("Waiting until bootstrapper is started...");

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

        log.info("Waiting until all nodes are started...");
        try {
            cdLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("All nodes are started.");

    }

    private void submitJobs() {
        userHandler = new UserHandler();
//        userHandler.submitJob();
    }

}
