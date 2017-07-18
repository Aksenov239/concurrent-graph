package test.workers;

import abstraction.DynamicGraph;
import test.Measure;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by vaksenov on 17.07.2017.
 */
public class TreeWorker extends Worker {
    volatile boolean stop;

    Random rnd;
    DynamicGraph graph;
    int size;
    int connectedRatio;
    ArrayList<Measure.Edge> edges;

    public TreeWorker(int id, DynamicGraph graph, int size, int connectedRatio, ArrayList<Measure.Edge> edges) {
        rnd = new Random(id);
        this.graph = graph;
        this.size = size;
        this.connectedRatio = connectedRatio;
        this.edges = edges;
    }

    public void run() {
        while (!stop) {
            int percent = rnd.nextInt(100);
            if (percent > 100 - connectedRatio) {
                int u = rnd.nextInt(size - 1) + 1;
                int v = rnd.nextInt(u);

                if (graph.isConnected(v, u)) {
                    successfulConnected++;
                }
                numConnected++;
            } else {
                Measure.Edge e = edges.get(rnd.nextInt(edges.size()));
                if (percent < (100 - connectedRatio) / 2) {
                    if (graph.addEdge(e.v, e.u)) {
                        successfulAdd++;
                    }
                    numAdd++;
                } else {
                    if (graph.removeEdge(e.v, e.u)) {
                        successfulRemove++;
                    }
                    numRemove++;
                }
            }
        }
    }

    public void stop() {
        stop = true;
    }
}
