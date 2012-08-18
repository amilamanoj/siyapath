import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.siyapath.NodeContext;
import org.siyapath.SiyapathNode;
import org.siyapath.service.NodeStatus;

import java.util.concurrent.CountDownLatch;

/**
 * Created by IntelliJ IDEA.
 * User: Amila Manoj
 * Date: 6/21/12
 * Time: 10:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class SiyapathNodeController {

    private static final Log log = LogFactory.getLog(SiyapathNodeController.class);

    private SiyapathNode siyapathNode;
    private NodeThread nodeThread;
    private CountDownLatch latch;

    SiyapathNodeController(SiyapathNode siyapathNode, CountDownLatch latch) {
        this.siyapathNode = siyapathNode;
        this.nodeThread = new NodeThread(siyapathNode.getNodeContext().getNodeInfo().toString());
        this.latch = latch;
    }

    public void start() {
        log.info("Starting node: " + siyapathNode.getNodeContext().getNodeInfo().getNodeId());
        nodeThread.start();

        while (!siyapathNode.getNodeContext().getNodeResource().isIdle()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void startNonBlocking() {
        log.info("Starting node: " + siyapathNode.getNodeContext().getNodeInfo().getNodeId());
        nodeThread.start();
    }

    public boolean isStarted() {
        return siyapathNode.getNodeContext().getNodeResource().isIdle();
    }

    class NodeThread extends Thread {

        NodeThread(String name) {
            super(name);
        }

        public void run() {
            siyapathNode.startSiyapathNode();
            if (latch != null) {
                latch.countDown();
            }
        }
    }
}
