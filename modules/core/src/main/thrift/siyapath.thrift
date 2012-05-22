
namespace java org.siyapath.service

/**
 * Contains information about a particular node
 */
struct NodeData {
    1: i32 nodeID,
    2: string ip,
    3: i32 port
}

/**
 * Contains information about a particular node
 */
struct NodeResourceData {
    1: i32 nodeID,
    2: NodeData nodeData,
    3: map<string,string> nodeProperties
}

/**
 * Contains information about a Task
 */
struct Task {
    1: i32 taskID,
    2: i32 jobID,
    3: binary taskProgram,
    4: string taskData,
    5: string className,
    6: NodeData sender,
    7: string taskResult
}

struct Job {
    1: i32 sender,
    2: i32 recipient
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
service Siyapath {

    //Gossiping node resource data
    GossipData gossip(1:GossipData gData),

    //Called by a node upon joining the network
    bool notifyPresence(1:NodeData nodeData),

    //Gossiping known member list
    set<NodeData> memberDiscovery(1:set<NodeData> knownNodes),

    //Retrieving current set of known members. TODO: review the return type 'set'
    set<NodeData> getMembers(),

    //ping to see if member is alive
    bool isAlive(),

    //request a member to become a backup node for given task
    bool requestBecomeBackup(1:i32 nodeID, 2:i32 taskID ),

    //Submitting a job
    string submitJob(1:i32 jobID 2:NodeData user, 3:map<i32,Task> tasks ),

    //Submitting a task
    bool submitTask(1:Task task),

    //Getting current status of a job
    string getJobStatus(1:i32 jobID),

    //Getting the computation result of a job
    map<i32,Task> getJobResult (1:i32 jobID),

    //Sending task result to job-distributing node
    bool sendTaskResult (1:Task task),

    //User authentication
    string userLogin(1:string username, 2:string password)
}
