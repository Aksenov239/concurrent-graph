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
                graph.isConnected(v, u);
                numConnected++;
            } else if (percent < (100 - connectedRatio)) {
                graph.addEdge(v, u);
                numAdd++;
            } else {
                graph.removeEdge(v, u);
                numRemove++;
            }
        }
    }

    public void stop() {
        stop = true;
    }
}
