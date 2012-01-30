
namespace java siyapath

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
 * Service for communication based on gossip protocols
 */
service GossipService {

  GossipData Gossip(1:GossipData gData)

}

/**
 * Service for getting current set of known members
 */
service MemberService{
  set<string> getMembers()
}