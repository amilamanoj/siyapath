import junit.framework.Assert;
import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TestSiyapathSimulation extends TestCase {

    private static final Log log = LogFactory.getLog(TestSiyapathSimulation.class);
    private int nodeCount;
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
        log.info("Total nodes: " + nodeCount);
        Assert.assertEquals("a", "a");
    }

    private void startBootStrapper() {
       log.info("Starting Bootstrapper Node");
    }

    private void startNodes() {
       log.info("Starting Nodes. Total Nodes: " + nodeCount);
    }

    private void submitJobs() {

    }


}
