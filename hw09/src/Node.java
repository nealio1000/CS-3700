/* Node.java */
/**
 * Node object for use in a graph
 * @author Neal Friedman
 * @version CS3700 Hw09
 */
public class Node {
    /** The id of this node. */
    private int nodeId;

    /**
     * Constructs a state with the specified name and label.
     * The activity counter is set to zero.
     * @param id the name for this state
     */
    public Node(final int id) {
        this.nodeId = id;
    }

    /**
     * Retrieves the ID of the node
     * @return the ID of the node
     */
    public int getId() {
        return this.nodeId;
    }

    @Override
    public String toString() {
        return "<V" + nodeId +">";
    }
}