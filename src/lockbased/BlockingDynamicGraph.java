package lockbased;

import sequential.SequentialDynamicGraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

/**
 * User: Aksenov Vitaly
 * Date: 12.07.2017
 * Time: 14:36
 */
public class BlockingDynamicGraph implements abstraction.DynamicGraph {

    SequentialDynamicGraph sdg;

    ReentrantLock lock = new ReentrantLock();

    public BlockingDynamicGraph(int n, int threads) {
        sdg = new SequentialDynamicGraph(n, 1);
    }

    public void clear() {
        sdg.clear();
    }

    public boolean isConnected(int u, int v) {
        lock.lock();
        boolean result = sdg.isConnected(u, v);
        lock.unlock();
        return result;
    }

    public boolean addEdge(int u, int v) {
        lock.lock();
        boolean result = sdg.addEdge(u, v);
        lock.unlock();
        return result;
    }

    public boolean removeEdge(int u, int v) {
        lock.lock();
        boolean result = sdg.removeEdge(u, v);
        lock.unlock();
        return result;
    }



}
