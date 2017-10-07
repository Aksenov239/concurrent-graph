package test;

import abstraction.DynamicGraph;
import test.workers.RandomWorker;
import test.workers.TreeWorker;
import test.workers.Worker;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

/**
 * Created by vaksenov on 17.07.2017.
 */
public class Measure {
    int threads;
    int connectedRatio;
    int warmup;
    int duration;
    int size;
    int iterations;
    String benchClassname;
    String workerType = "tree";

    DynamicGraph graph;

    ArrayList<Edge>[] initialTrees;

    public class Edge {
        public int u, v;

        public Edge(int u, int v) {
            this.u = u;
            this.v = v;
        }
    }

    public void setup(String workerType) { // Setup the graph with tree
        Random rnd = new Random(239);
        if (workerType.equals("tree")) {
            initialTrees = new ArrayList[1];
        } else if (workerType.equals("trees")) {
            initialTrees = new ArrayList[threads];
        } else if (workerType.equals("graph")) {
            initialTrees = new ArrayList[threads];
        }
        for (int t = 0; t < initialTrees.length; t++) {
            initialTrees[t] = new ArrayList<>();
            for (int i = 1; i < size; i++) {
                Edge e = new Edge(i, rnd.nextInt(i));
                initialTrees[t].add(e);
                if (rnd.nextBoolean()) {
                    graph.addEdge(e.u, e.v);
                }
            }
        }
    }

    public void evaluateFor(int milliseconds, boolean withStats) {
        setup(workerType);
        graph.reinitialize();

        System.out.println("Setup finished");

        Thread[] thrs = new Thread[threads];
        Worker[] workers = new Worker[threads];
        for (int i = 0; i < threads; i++) {
            if (workerType.equals("tree")) {
                workers[i] = new TreeWorker(i, graph, size, connectedRatio, initialTrees[0]);
            } if (workerType.equals("trees")) {
                workers[i] = new TreeWorker(i, graph, size, connectedRatio, initialTrees[i]);
            } else {
                workers[i] = new RandomWorker(i, graph, size, connectedRatio);
            }
            thrs[i] = new Thread(workers[i]);
        }

        long start = System.nanoTime();

        for (int i = 0; i < threads; i++) {
            thrs[i].start();
        }

        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < threads; i++) {
            workers[i].stop();
        }

        for (int i = 0; i < threads; i++) {
            try {
                thrs[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        double totalTime = 1. * (System.nanoTime() - start) / 1_000_000_000;

        if (!withStats) {
            System.out.println("Total time spent:    \t" + totalTime);
            graph.clear();
            return;
        }

        int totalAdd = 0;
        int successfulAdd = 0;
        int totalRemove = 0;
        int successfulRemove = 0;
        int totalConnected = 0;
        int successfulConnected = 0;
        for (int i = 0; i < threads; i++) {
            totalAdd += workers[i].numAdd;
            successfulAdd += workers[i].successfulAdd;
            totalRemove += workers[i].numRemove;
            successfulRemove += workers[i].successfulRemove;
            totalConnected += workers[i].numConnected;
            successfulConnected += workers[i].successfulConnected;
        }

        Locale.setDefault(Locale.US);

        String result = "Results:\n" + "-----------------\n" +
                "Total time spent:      \t" + totalTime + "\n" +
                "Throughput:            \t" + (totalAdd + totalRemove + totalConnected) / totalTime + " ops/sec" + "\n" +
                "Total operations:      \t" + (totalAdd + totalRemove + totalConnected) + "\n" +
                " -- Total add:         \t" + totalAdd + "\n" +
                "     | successful      \t" + (100. * successfulAdd / totalAdd) + " %\n" +
                " -- Total remove:      \t" + totalRemove + "\n" +
                "     | successful      \t" + (100. * successfulRemove / totalRemove) + " %\n" +
                " -- Total isConnected: \t" + totalConnected + "\n" +
                "     | successful      \t" + (100. * successfulConnected / totalConnected) + " %";

        System.out.println(result);

        graph.clear();
    }

    public void run() {
        printParams();

        try {
            Class<DynamicGraph> graphClazz = (Class<DynamicGraph>) Class.forName(benchClassname);
            graph = graphClazz.getConstructor(int.class, int.class).newInstance(size, threads);

            if (warmup > 0) {
                System.out.println("Warmup started");
                evaluateFor(warmup, false);
                System.out.println("Warmup finished");
            }

            for (int i = 0; i < iterations; i++) {
                System.out.println("Start iteration " + (i + 1));
                evaluateFor(duration, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Cannot find benchmark class: " + benchClassname);
            System.exit(1);
        }
    }

    public Measure(String[] args) {
        parseCommandLine(args);
    }

    private void parseCommandLine(String[] args) {
        int argNumber = 0;
        while (argNumber < args.length) {
            String parameter = args[argNumber++];
            try {
                String option = args[argNumber++];
                switch (parameter) {
                    case "-t":
                        threads = Integer.parseInt(option);
                        break;
                    case "-c":
                        connectedRatio = Integer.parseInt(option);
                        break;
                    case "-w":
                        warmup = Integer.parseInt(option);
                        break;
                    case "-d":
                        duration = Integer.parseInt(option);
                        break;
                    case "-s":
                        size = Integer.parseInt(option);
                        break;
                    case "-b":
                        benchClassname = option;
                        break;
                    case "-n":
                        iterations = Integer.parseInt(option);
                        break;
                    case "-wt":
                        workerType = option;
                        break;
                }
            } catch (IndexOutOfBoundsException e) {
                System.err.println("Missing value after option " + parameter + ". Ignoring.");
            } catch (NumberFormatException e) {
                System.err.println("Expecting number after option " + parameter + ". Ignoring.");
            }
        }
    }

    private void printParams() {
        String params = "Benchmark parameters" + "\n" + "-------------------\n" +
                "Number of threads:\t" + threads + "\n" +
                "Iterations:       \t" + iterations + "\n" +
                "Length:           \t" + duration + "\n" +
                "Warmup:           \t" + warmup + "\n" +
                "Initial size:     \t" + size + "\n" +
                "Connectivity:     \t" + connectedRatio + "%\n" +
                "Benchmark:        \t" + benchClassname;
        System.out.println(params);
    }

    public static void main(String[] args) {
        new Measure(args).run();
    }

}
