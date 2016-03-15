/* Graph.java */
/**
 * A graph data structure that has a list of
 * nodes and maintains a table of the
 * relationships between those nodes
 * @author Neal Friedman
 * @version CS3700 Hw09
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Graph{

    private ArrayList<Node> nodes = new ArrayList<Node>();
    private Map<Node, Map<Node, Integer>> transition = new HashMap<Node, Map<Node, Integer>>();
    private Node start;
    private Node finish;
    private Node current;
    /**
     * Constructs a graph with a list of all the nodes, a transition table,
     * and a starting node
     *
     @param nodes an arraylist of all nodes
     @param transition the transition function of this graph
     @param start the start state of this graph
     @param finish the finish state of this graph
     */

    public Graph(ArrayList<Node> nodes, Map<Node, Map<Node, Integer>> transition,
               Node start, Node finish) {
        this.nodes = nodes;
        this.transition = transition;
        this.start = start;
        this.finish = finish;
    }

    public Graph(){};

    /** Retrieves the ArrayList of all nodes.
     * @return The ArrayList of all nodes
     */
    public ArrayList<Node> nodes() {
        return nodes;
    }

    /** Retrieves the starting node
     * @return the initial node; null if none
     */
    public Node getStartNode() {
        if (start == null)
            return null;
        else
            return start;
    }

    public Node getFinishNode(){
        if(finish == null)
            return null;
        else
            return finish;
    }

    /** Retrieves the transition function of this graph.
     * @return the transition function
     */
    public Map<Node, Map<Node, Integer>> transitionFunction() {
        return transition;
    }

    /**
     * Inefficient way to see if a node with a specific id exists
     * @param id The ID of the node
     * @return The node if it exists, null if it does not
     */
    public Node getNode(int id){
        for(int i = 0; i < nodes().size();i++){
            if(nodes.get(i).getId() == id)  //node exists
                return nodes.get(i);
        }
        return null; //Potentially horrible mistake right here
    }

    /**
     * Build the graph from the array that was generated from parsing
     * the data file
     * @param routers The array of routers to build the graph from.
     * @return Returns the graph of all the nodes with their transition functions
     */
    public Graph build(int[][] routers){

        for(int i = 0; i < routers.length; i++) {
            if(getNode(routers[i][0]) == null)
            {
                Node n = new Node(routers[i][0]);
                nodes.add(n);
            }
            if(getNode(routers[i][1]) == null)
            {
                Node n = new Node(routers[i][1]);
                nodes.add(n);
            }
        }
        for(int i = 0; i < routers.length; i++){
            Map<Node, Integer> trans = new HashMap<>();
            if(transition.containsKey(getNode(routers[i][0]))){
                transition.get(getNode(routers[i][0])).put(getNode(routers[i][1]), routers[i][2]);
            }
            else {
                trans.put(getNode(routers[i][1]), routers[i][2]);
                transition.put(getNode(routers[i][0]), trans);
            }
        }

        // ******* BEWARE!!!! YOU HARDCODED THE START AND FINISH NODES!!!!! ******************
        return new Graph(nodes, transition, getNode(routers[0][0]), getNode(routers[23][0]));
    }

    public static int getCost(Graph g, Node a, Node b){;
        return g.transitionFunction().get(a).get(b);
    }
}
