
namespace java org.siyapath.service

enum NodeStatus {
  CREATED = 1,
  STARTING = 2,
  BUSY = 3,
  IDLE = 4
}

/**
 * Contains information about a particular node
 */
struct NodeData {
    1: i32 nodeID,
    2: string ip,
    3: i32 port,
    4: NodeStatus nodeStatus
}


/**
 * Contains information about a particular node
 */
struct NodeResourceData {
    1: NodeData nodeData,
    2: map<string,string> nodeProperties
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
    7: NodeData backup,
    8: string requiredResources
}

struct Job {
    1: i32 jobID,
    2: NodeData user,
    3: map<i32,Task> tasks
}

/**
 * Service for communication related to gossip protocols
 */
service Siyapath {

    //Gossiping node resource data
    NodeResourceData resourceGossip(1:NodeResourceData resourceData),

    //Called by a node upon joining the network
    bool notifyPresence(1:NodeData nodeData),

    //Gossiping known member list
    set<NodeData> memberDiscovery(1:NodeData nodeData, 2:set<NodeData> knownNodes),

    //Retrieving current set of known members. TODO: review the return type 'set'
    set<NodeData> getMembers(),

    //ping to see if member is alive
    bool isAlive(),

    //request a member to become a backup node for given job
    bool requestBecomeBackup(1:i32 jobID, 2:NodeData node ),

    //Submitting a job
    string submitJob(1:Job job),

    //Submitting a task
    bool submitTask(1:Task task),

    //Getting current status of a job
    bool getJobStatusFromJobHandler(1:i32 jobID, 2:i32 port),

    //Getting current status of a job
    bool getTaskStatusFromTaskProcessor(1:Task task, 2:i32 port),

    //Getting the computation result of a job
    map<i32,Task> getJobResult (1:i32 jobID),

    //Sending task result to job-distributing node
    bool sendTaskResult (1:Task task),

    //User authentication
    string userLogin(1:string username, 2:string password)
}
