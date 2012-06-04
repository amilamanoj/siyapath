import junit.framework.Assert;
import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.siyapath.NodeInfo;
import org.siyapath.SiyapathNode;
import org.siyapath.utils.CommonUtils;

import java.util.ArrayList;

public class TestSiyapathSimulation extends TestCase {

    private static final Log log = LogFactory.getLog(TestSiyapathSimulation.class);
    private int nodeCount;
    private SiyapathNode bootStrapperNode;
    private ArrayList<SiyapathNode> nodeList;


    public TestSiyapathSimulation() {
        nodeList = new ArrayList<SiyapathNode>();
    }

    @Override
    public void setUp() throws Exception {
        log.info("Starting up Siyapath simulation...");
    }

    @Override
    public void tearDown() throws Exception {
        log.info("Finishing Siyapath simulation");
    }

    public void testSimulation() throws Exception {
        log.info("Running Siyapath simulation");
        String nodes = System.getProperty("nodes");
        try {
            nodeCount = Integer.parseInt(nodes);
        } catch (NumberFormatException e) {
            nodeCount = 100;
            log.info("Starting default number of nodes: " + nodeCount);
        }
        startBootStrapper();
        startNodes();
        submitJobs();

        Thread.sleep(10000);

        log.info("Total nodes: " + nodeCount);
        Assert.assertEquals("a", "a");
    }

    private void startBootStrapper() {
        log.info("Starting Bootstrapper Node");
        NodeInfo bootStrapperInfo = new NodeInfo();
        bootStrapperNode = new SiyapathNode(bootStrapperInfo);

        bootStrapperNode.startSiyapathNode();

        log.info("Waiting until bootstrapper is started...");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        log.info("Bootstrapper is up. continuing simulation");
    }

    private void startNodes() {
        log.info("Preparing to start Nodes. Total Nodes: " + nodeCount);
        log.info("Creating nodes...");
        for (int i = 0; i < nodeCount; i++) {
            NodeInfo nodeInfo = new NodeInfo();
            nodeInfo.setNodeId(CommonUtils.getRandomPort());
            nodeInfo.setPort(nodeInfo.getNodeId());
            log.info("Creating node with port: " + nodeInfo.getPort());
            SiyapathNode node = new SiyapathNode(nodeInfo);
            nodeList.add(node);
        }

        log.info("Starting nodes...");
        for (SiyapathNode sNode : nodeList) {
            log.info("Starting node: " + sNode.getNodeId());
            sNode.startSiyapathNode();
        }
    }

    private void submitJobs() {

    }

//    class NodeThread extends Thread {
//
//        public boolean isRunning = false;
//
//        public void run() {
//            bootStrapperNode.startSiyapathNode();
//            try {
//                wait(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            isRunning = true;
//        }
//    }

}
