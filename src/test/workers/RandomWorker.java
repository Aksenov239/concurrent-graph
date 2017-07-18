package test.workers;

import abstraction.DynamicGraph;

import java.util.Random;

/**
 * Created by vaksenov on 17.07.2017.
 */
public class RandomWorker extends Worker {
    volatile boolean stop;

    Random rnd;
    DynamicGraph graph;
    int size;
    int connectedRatio;

    public RandomWorker(int id, DynamicGraph graph, int size, int connectedRatio) {
        rnd = new Random(id);
        this.graph = graph;
        this.size = size;
        this.connectedRatio = connectedRatio;
    }

    public void run() {
        while (!stop) {
            int u = rnd.nextInt(size - 1) + 1;
            int v = rnd.nextInt(u);

            int percent = rnd.nextInt(100);
            if (percent > 100 - connectedRatio) {
                if (graph.isConnected(v, u)) {
                    successfulConnected++;
                }
                numConnected++;
            } else if (percent < (100 - connectedRatio)) {
                if (graph.addEdge(v, u)) {
                    successfulAdd++;
                }
                numAdd++;
            } else {
                if (graph.removeEdge(v, u)) {
                    successfulRemove++;
                }
                numRemove++;
            }
        }
    }

    public void stop() {
        stop = true;
    }
}
