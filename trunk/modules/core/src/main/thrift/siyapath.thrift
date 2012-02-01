
namespace java org.siyapath

/**
 * Contains information about a particular node
 */
struct NodeData {
    1: string timestamp,
    2: map<string,string> nodeData
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
    set<string> getMembers(),

    //Check if node is alive
    void isAlive(),

    //Requesting to be the backup for the invoking node
    bool backupRequest(),
}