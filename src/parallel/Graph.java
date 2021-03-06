package parallel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph {

        private Map<String, Node> nodes = new HashMap<String, Node>();

        public Map<String, Node> getNodes() {
                return nodes;
        }

        public void setNodes(Map<String, Node> nodes) {
                this.nodes = nodes;
        }

        public Graph() {}

        public void addEdge(String nodeName1, String nodeName2) {
                Node node1 = nodes.get(nodeName1);
                if (node1 == null) {
                  node1 = new Node(nodeName1);
                }

                Node node2 = nodes.get(nodeName2);
                if (node2 == null) {
                  node2 = new Node(nodeName2);
                }

                node1.addNeighbor(node2);
                node2.addNeighbor(node1);

                nodes.put(nodeName1, node1);
                nodes.put(nodeName2, node2);
        }
  
        public List<String> shortestPath(String startNodeName, String endNodeName) {
                // key node, value parent
                Map<String, String> parents = new HashMap<String, String>();
                List<Node> temp = new ArrayList<Node>();
                // Get the start node from the map
                Node start = nodes.get(startNodeName);
                // Add the start node to the temporal list of nodes
                temp.add(start);
                // Add the start node to map of parents
                // The starting node has no parents
                parents.put(startNodeName, null);
                while (temp.size() > 0) {
                        // Get the first node of the list of nodes
                        Node currentNode = temp.get(0);
                        // Get the neighbord of the current node
                        List<Node> neighbors = currentNode.getNeighbors();
                        // Iterate through all the neighbors of the current node
                        for (int i = 0; i < neighbors.size(); i++) {
                                // Get the first neighbor
                                Node neighbor = neighbors.get(i);
                                // Get the name of this neighbor
                                String nodeName = neighbor.getName();

                                // a node can only be visited once if it has more than one parents
                                boolean visited = parents.containsKey(nodeName);
                                if (visited) {
                                        String parent = parents.get(nodeName);
                                        //System.out.printf("Parent = %s, Child = %s, visited\n", currentNode.getName(), nodeName);
                                        continue;
                                } else {
                                        // Add the children whose neighbors or own children will be explored
                                        temp.add(neighbor);
                                        // parents map can be used to get the path
                                        // Insert the neighbors with their parents
                                        // put(B, A);
                                        // put(C, A);
                                        parents.put(nodeName, currentNode.getName());
                                        String child = nodeName;
                                        String parent = parents.get(nodeName);
                                        //System.out.printf("Parent = %s, Child = %s\n", parent, child);
                                        // return the shortest path if end node is reached
                                        if (nodeName.equals(endNodeName)) {
                                                //System.out.println(parents);
                                                return getPath(parents, endNodeName);
                                        }
                                }
                        }
                        temp.remove(0);
                }
                return null;
        }
        /*
        private static Map<String, String> parents;
        private static List<Node> temp;
        private static String endNodeName;
        private static List<String> path;
        private static boolean foundShortestPath;
  
        public static List<String> shortestPathWrapper(Map<String, Node> nodes, String startNodeName, String endNodeNameParam) {
                parents = new HashMap<String, String>();
                temp = new ArrayList<Node>();
                endNodeName = endNodeNameParam;
                path = new ArrayList<String>();
                
                foundShortestPath = false;
                // Get the start node from the map
                Node start = nodes.get(startNodeName);
                // Add the start node to the temporal list of nodes
                temp.add(start);
                // Add the start node to map of parents, the starting node has no parents
                parents.put(startNodeName, null);
                // Initialize the end node
                Graph.endNodeName = endNodeName;
                explorePendingNodes();
                if(foundShortestPath){
                        return path;
                }
                return null;
        }
  
        public static List<String> explorePendingNodes() {
                if (temp.size() > 0) {
                        // Get the first node of the list of nodes
                        Node currentNode = temp.get(0);
                        // Get the neighbord of the current node
                        List<Node> neighbors = currentNode.getNeighbors();
                        // Explore all the neighbors of the current node
                        exploreNeighbors(neighbors, currentNode);
                        if(foundShortestPath){
                                return path;
                        }
                        temp.remove(0);
                        explorePendingNodes();
                }
                return null;
        }
  
        public static List<String> exploreNeighbors(List<Node> neighbors, Node currentNode) {
                // Explore all the neighbors of the current node
                if (neighbors.size() > 0) {
                        // Get the first neighbor
                        Node neighbor = neighbors.get(0);
                        // Get the name of this neighbor
                        String nodeName = neighbor.getName();
                        // a node can only be visited once if it has more than one parents
                        boolean visited = parents.containsKey(nodeName);
                        if (visited) {
                                String parent = parents.get(nodeName);
                                System.out.printf("Parent = %s, Child = %s, visited\n", currentNode.getName(), nodeName);
                        } else {
                                // Add the children whose neighbors or own children will be explored
                                temp.add(neighbor);
                                // parents map can be used to get the path
                                // Insert the neighbors with their parents
                                // put(B, A);
                                // put(C, A);
                                parents.put(nodeName, currentNode.getName());
                                String child = nodeName;
                                String parent = parents.get(nodeName);
                                System.out.printf("Parent = %s, Child = %s\n", parent, child);
                                // return the shortest path if end node is reached
                                if (nodeName.equals(endNodeName)) {
                                        //System.out.println(parents);
                                        setPath();
                                        foundShortestPath = true;
                                        return path;
                                }
                        }
                        neighbors.remove(0);
                        exploreNeighbors(neighbors, currentNode);
                }
                return null;
                
        }
  
        private static void setPath() {
                String node = endNodeName;
                while (node != null) {
                        path.add(0, node);
                        String parent = parents.get(node);
                        node = parent;
                }
        }
        */
        
        private List<String> getPath(Map<String, String> parents, String endNodeName) {
                List<String> path = new ArrayList<String>();
                String node = endNodeName;
                while (node != null) {
                        path.add(0, node);
                        String parent = parents.get(node);
                        node = parent;
                }
                return path;
        }
}

        class Node {
                String name;
                List<Node> neighbors = new ArrayList<Node>();

                public Node(String name) {
                        this.name = name;
                }

                public String getName() {
                        return this.name;
                }

                public void addNeighbor(Node neighbor) {
                        neighbors.add(neighbor);
                }

                public List<Node> getNeighbors() {
                        return neighbors;
                }

                public String toString() {
                        return this.name;
                }
        }