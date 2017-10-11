package test;

import sequential.SequentialDynamicGraph;

import java.io.PrintWriter;
import java.util.Scanner;

/**
 * User: Aksenov Vitaly
 * Date: 12.07.2017
 * Time: 17:16
 */
public class Test {
    public static void main(String[] args) {
        new Test().run();
    }

    public void run() {
        Scanner in = new Scanner(System.in);
        PrintWriter out = new PrintWriter(System.out);

        int n = in.nextInt();
        int q = in.nextInt();

        SequentialDynamicGraph sdg = new SequentialDynamicGraph(n, 1);

        for (int i = 0; i < q; i++) {
            String query = in.next();

            if (query.equals("?")) {
                out.println(sdg.numberOfCC());
                continue;
            }

            int u = in.nextInt() - 1;
            int v = in.nextInt() - 1;

            if (query.equals("c")) {
                out.println(sdg.isConnected(u, v));
            } else if (query.equals("+")) {
                sdg.addEdge(u, v);
            } else {
                sdg.removeEdge(u, v);
            }
        }

        out.close();
    }
}
