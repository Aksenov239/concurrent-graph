package test;

import sequential.SequentialDynamicGraph;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * User: Aksenov Vitaly
 * Date: 12.07.2017
 * Time: 17:16
 */
public class TestLoad {
    public static void main(String[] args) {
        new TestLoad().run();
    }

    public class Edge {
        int u, v;

        public Edge(int u, int v) {
            this.u = u;
            this.v = v;
        }
    }

    Random rnd = new Random(239);
    public ArrayList<Edge>[] trees;

    public void generateTrees(int n, int k) {
        trees = new ArrayList[k];
        for (int i = 0; i < k; i++) {
            trees[i] = new ArrayList<>();

            for (int j = 1; j < n; j++) {
                trees[i].add(new Edge(rnd.nextInt(j), j));
            }
        }
    }

    public void run() {
        int n = 10;
        int k = 3;

        SequentialDynamicGraph sdg = new SequentialDynamicGraph(n, 1);

        generateTrees(n, k);

        while (true) {
            for (int i = 0; i < trees.length; i++) {
                Edge e = trees[i].get(rnd.nextInt(trees[i].size()));

                if (rnd.nextBoolean()) {
//                    System.err.println("Add " + e.u + " " + e.v);
                    sdg.addEdge(e.u, e.v);
                } else {
//                    System.err.println("Remove " + e.u + " " + e.v);
                    sdg.removeEdge(e.u, e.v);
                }
            }
        }

    }
}
