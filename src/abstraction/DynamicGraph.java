package abstraction;

/**
 * User: Aksenov Vitaly
 * Date: 12.07.2017
 * Time: 14:35
 */
public interface DynamicGraph {
    public boolean isConnected(int u, int v);
    public boolean addEdge(int u, int v);
    public boolean removeEdge(int u, int v);

    public void clear();
    public default void reinitialize() {}
}
