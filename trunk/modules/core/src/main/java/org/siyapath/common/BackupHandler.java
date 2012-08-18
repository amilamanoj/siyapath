package org.siyapath.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.siyapath.NodeContext;
import org.siyapath.NodeInfo;

public class BackupHandler {

    private static final Log log = LogFactory.getLog(BackupHandler.class);

    private NodeContext context;
    private int jobID;
    private NodeInfo nodeToBackup;

    public BackupHandler(NodeContext nodeContext) {
        this.context = nodeContext;
    }

    /**
     * @param jobID        JobID
     * @param nodeToBackup Node that has to be backed-up
     * @return true if request is accepted, false otherwise
     */
    public boolean requestBecomeBackup(int jobID, NodeInfo nodeToBackup) {
        if (context.isBackup() || !context.getNodeResource().isIdle()) {
            return false;
        }
        this.jobID = jobID;
        this.nodeToBackup = nodeToBackup;
        context.setBackup(true);

        return true;
    }

    public void pushCurrentResult() {

    }

    /**
     * @param processingNodeID
     * @param jobID
     */
    public void endBackup(int processingNodeID, int jobID) {

    }


}
