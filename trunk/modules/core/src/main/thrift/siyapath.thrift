
namespace java org.siyapath.service

enum NodeStatus {
  CREATED = 1,
  STARTING = 2,
  PROCESSING_IDLE = 3,
  PROCESSING_BUSY = 4,
  DISTRIBUTING = 5,
  BUSY = 6,
  IDLE = 7
}


enum ResourceLevel {
        LOW = 1,
        MEDIUM = 2,
        HIGH = 3
}


enum TaskStatus {
  DISPATCHING = 1,
  PROCESSING = 2,
  DONE = 3
}

/**
 * Contains information about a particular node
 */
struct NodeData {
    1: i32 nodeID,
    2: string ip,
    3: i32 port,
}


/**
 * Contains information about a particular node
 */
struct NodeResourceData {
    1: NodeData nodeData,
    2: ResourceLevel resourceLevel,
    3: NodeStatus nodeStatus,
    4: i64 timeStamp
}

/**
 * Contains information about a Task
 * optional value is set at task collector for each task a job belongs to
 */
struct Task {
    1: i32 taskID,
    2: i32 jobID,
    3: binary taskProgram,
    4: binary taskData,
    5: string className,
    6: NodeData sender,
    7: NodeData backup,
    8: string requiredResourceLevel,
    9: i32 taskReplicaCount
}

/**
 * A job has a set of tasks
 */
struct Job {
    1: i32 jobID,
    2: NodeData user,
    3: map<i32,Task> tasks,
    4: i32 replicaCount
}


struct Result {
    1: i32 jobID,
    2: i32 taskID,
    3: binary results,
    4: NodeData processingNode
}

struct TaskResult {
    1: TaskStatus status,
    2: binary results
}

/**
 * Service for communication related to gossip protocols
 */
service Siyapath {

    //Gossiping node resource data
    map<i32,NodeResourceData> resourceGossip(1:map<i32,NodeResourceData> knownResourceNodes),

    //Called by a node upon joining the network
    bool notifyPresence(1:NodeData nodeData),

    //Gossiping known member list
    set<NodeData> memberDiscovery(1:NodeData nodeData, 2:set<NodeData> knownNodes),

    //Retrieving current set of known members. TODO: review the return type 'set'
    set<NodeData> getMembers(),

    //ping to see if member is alive
    bool isAlive(),

    //request a member to become a backup node for given job
    bool requestBecomeBackup(1:Job job, 2:NodeData node ),

    //Stop backing-up when job is complete
    void endBackup(),

    //Submitting a job
    bool submitJob(1:Job job),

    //Submitting a task
    bool submitTask(1:Task task),

    //new job status polling
    map<i32,TaskResult> getJobStatus(1:i32 jobId),

    //Getting the computation result of a job
    map<i32,TaskResult> getJobResult (1:i32 jobID),

    //Sending task result to job-distributing node
    bool sendTaskResult (1:Result result),

    //Sending task result to backup node by job-distributing node
    bool sendTaskResultToBackup (1:Result result),

    //Notify job-distributing node that the task processor is alive
    void notifyTaskLiveness (1:i32 taskID),

    //User authentication
    string userLogin(1:string username, 2:string password),

    //Retrieving current Node Status
    NodeStatus getNodeStatus()
}
