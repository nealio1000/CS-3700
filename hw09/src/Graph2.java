import java.util.List;

/**
 * Created by neal on 4/25/15.
 */
public class Graph2 {
    private final List<Vertex> vertexes;
    private final List<Edge> edges;

    public Graph2(List<Vertex> vertexes, List<Edge> edges) {
        this.vertexes = vertexes;
        this.edges = edges;
    }

    public List<Vertex> getVertexes() {
        return vertexes;
    }

    public List<Edge> getEdges() {
        return edges;
    }
}


