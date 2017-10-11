package lockbased;

import sequential.SequentialDynamicGraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * User: Aksenov Vitaly
 * Date: 12.07.2017
 * Time: 14:36
 */
public class BlockingRWDynamicGraph implements abstraction.DynamicGraph {
    SequentialDynamicGraph sdg;
    ReadWriteLock lock = new ReentrantReadWriteLock();

    public BlockingRWDynamicGraph(int n, int threads) {
        sdg = new SequentialDynamicGraph(n, 1);
    }

    public void clear() {
        sdg.clear();
    }

    public boolean isConnected(int u, int v) {
        lock.readLock().lock();
        boolean res = sdg.isConnected(u, v);
        lock.readLock().unlock();
        return res;
    }

    public boolean addEdge(int u, int v) {
        lock.writeLock().lock();
        boolean res = sdg.addEdge(u, v);
        lock.writeLock().unlock();
        return res;
    }

    public boolean removeEdge(int u, int v) {
        lock.writeLock().lock();
        boolean res = sdg.removeEdge(u, v);
        lock.writeLock().unlock();
        return res;
    }
}
