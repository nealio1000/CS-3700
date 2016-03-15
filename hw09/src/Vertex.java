/**
 * Created by neal on 4/25/15.
 */
public class Vertex {
    final private String id;
    final private String name;

    public Vertex(String id, String name) {
        this.id = id;
        this.name = name;
    }
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

}