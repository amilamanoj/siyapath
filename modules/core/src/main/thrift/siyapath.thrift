
namespace java org.siyapath

/**
 * Contains information about a particular node
 */
struct NodeData {
    1: string timestamp,
    2: map<string,string> nodeData
}

/**
 * Contains information about a Task
 */
struct Task {
    1: i32 taskID
}

/**
 * Data exchanged during gossip communication between two nodes
 */
struct GossipData {
    1: map<string, NodeData> gossipData
}

/**
 * Service for communication related to gossip protocols
 */
service GossipService {

    //Gossiping node resource data
    GossipData gossip(1:GossipData gData),

    //Gossiping known member list
    set<string> memberDiscovery(1:set<string> knownNodes),

    //Retrieving current set of known members
    set<string> getMembers()
}

/**
 * Service for computation related operations
 */
 service JobHandlerService {

    //Submitting a job
    string submitJob(1:map<i32,Task> tasks, 2:i32 jobID),

    //Getting current status of a job
    string getJobStatus(1:i32 jobID),

    //Getting the computation result of a job
    map<i32,Task> getJobResult (1:i32 jobID)
}
