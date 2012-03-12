
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
    1: string taskID,
    2: string jobID,
    3: binary taskProgram,
    4: string taskData,
    5: string className
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
    bool notifyPresence(1:i32 nodeID),

    //Gossiping known member list
    set<i32> memberDiscovery(1:set<i32> knownNodes),

    //Retrieving current set of known members. TODO: review the return type 'set'
    set<i32> getMembers(),

    //ping to see if member is alive
    bool isAlive(),

    //request a member to become a backup node for given task
    bool requestBecomeBackup(1:i32 nodeID, 2:i32 taskID ),

    //Submitting a job
    string submitJob(1:map<i32,Task> tasks, 2:i32 jobID),

    //Submitting a task
    bool submitTask(1:Task task),

    //Getting current status of a job
    string getJobStatus(1:i32 jobID),

    //Getting the computation result of a job
    map<i32,Task> getJobResult (1:i32 jobID),

    //User authentication
    string userLogin(1:string username, 2:string password)
}
