package abstraction;

/**
 * User: Aksenov Vitaly
 * Date: 12.07.2017
 * Time: 14:35
 */
public interface DynamicGraph {
    public boolean isConnected(int u, int v);
    public void addEdge(int u, int v);
    public void removeEdge(int u, int v);

    public void clear();
    public default void reinitialize() {}
}
