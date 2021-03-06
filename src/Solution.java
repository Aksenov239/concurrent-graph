import java.io.*;
import java.util.*;

/**
 * User: Aksenov Vitaly
 * Date: 12.07.2017
 * Time: 14:36
 */
public class Solution {
    Random rnd = new Random(239);

    public class Edge {
        int u, v, level;

        public Edge(int u, int v) {
            this.u = u;
            this.v = v;
            level = 0;
        }

        public int hashCode() {
            return u * 1_000_000 + v;
        }

        public boolean equals(Object o) {
            Edge e = (Edge) o;
            return e.u == u && e.v == v;
        }

        public String toString() {
            return u + " " + v;
        }
    }

    public enum NodeType {
        VERTEX,
        EDGE
    }

    public class Node {
        Node l, r, p;
        int y;
        int size;
        NodeType type;
        int id;
        int level;
        boolean hasVertex;
        boolean hasEdge;

        public Node(NodeType type, int id, int level) {
            y = rnd.nextInt();
            size = 1;
            this.type = type;
            this.id = id;
            this.level = level;
            hasVertex = false;
            hasEdge = false;
        }

        public void update() {
            size = getSizeNode(l) + getSizeNode(r) + 1;
            hasVertex = getHasVertexNode(l) || getHasVertexNode(r) || isHasVertex();
            hasEdge = getHasEdgeNode(l) || getHasEdgeNode(r) || isHasEdge();
            if (l != null) {
                l.p = this;
            }
            if (r != null) {
                r.p = this;
            }
        }

        public boolean isHasVertex() {
            if (type == NodeType.VERTEX) {
                return !adjacent[id][level].isEmpty();
            }
            return false;
        }

        public boolean isHasEdge() {
            if (type == NodeType.EDGE) {
                return edges.get(id).level == level;
            }
            return false;
        }

        public String toString() {
            String me = "";
            if (type == NodeType.EDGE) {
                me += edges.get(id).u + "->" + edges.get(id).v;
            } else {
                me += id;
            }

            return "[" + (l == null ? "" : l.toString() + ",") + me + (r == null ? "" : "," + r.toString()) + "]";
        }
    }

    public int getSizeNode(Node node) {
        return node == null ? 0 : node.size;
    }

    public boolean getHasVertexNode(Node node) {
        return node == null ? false : node.hasVertex;
    }

    public boolean getHasEdgeNode(Node node) {
        return node == null ? false : node.hasEdge;
    }

    public Node merge(Node l, Node r) {
        if (l == null) {
            return r;
        }
        if (r == null) {
            return l;
        }
        if (l.y > r.y) {
            l.r = merge(l.r, r);
            l.update();
            return l;
        } else {
            r.l = merge(l, r.l);
            r.update();
            return r;
        }
    }

    public Node[] split(Node v, int size) {
        if (v == null) {
            return new Node[]{null, null};
        }
        if (getSizeNode(v.l) >= size) {
            Node[] ns = split(v.l, size);
            v.l = ns[1];
            v.update();
            v.p = null;
            return new Node[]{ns[0], v};
        } else {
            Node[] ns = split(v.r, size - getSizeNode(v.l) - 1);
            v.r = ns[0];
            v.update();
            v.p = null;
            return new Node[]{v, ns[1]};
        }
    }

    public Node getRoot(Node v) {
        while (v.p != null) {
            v = v.p;
        }
        return v;
    }

    public int getPosition(Node v) {
        int sum = getSizeNode(v.l);
        while (v.p != null) {
            if (v.p.r == v) {
                sum += getSizeNode(v.p.l) + 1;
            }
            v = v.p;
        }
        return sum;
    }

    public class Forest {
        int level;
        Node[] vertexNode;
        HashMap<Edge, Node> nodeByEdge;

        public Forest(int n, int level) {
            this.level = level;
            nodeByEdge = new HashMap<>();
            vertexNode = new Node[n];
            for (int i = 0; i < n; i++) {
                vertexNode[i] = new Node(NodeType.VERTEX, i, level);
            }
        }

        public void updateToTop(Node v) {
            while (v != null) {
                v.update();
                v = v.p;
            }
        }

        public void makeFirst(Node v) {
            Node head = getRoot(v);
            int pos = getPosition(v);
            Node[] ns = split(head, pos);
            merge(ns[1], ns[0]);
        }

        public void link(int u, int v) {
            if (u > v) {
                int q = u;
                u = v;
                v = q;
            }

            Node n1 = getRoot(vertexNode[u]);
            Node n2 = getRoot(vertexNode[v]);
            makeFirst(vertexNode[u]);
            makeFirst(vertexNode[v]);

            int edgeId = edgeIndex.get(new Edge(u, v));
            Node c1 = new Node(NodeType.EDGE, edgeId, level);
            Node c2 = new Node(NodeType.EDGE, edgeId, level);
            nodeByEdge.put(new Edge(u, v), c1);
            nodeByEdge.put(new Edge(v, u), c2);

            merge(merge(merge(n1, c1), n2), c2);
        }

        public void cut(int u, int v) {
            makeFirst(vertexNode[u]);

            Edge l = new Edge(u, v);
            Edge r = new Edge(v, u);

            Node c1 = nodeByEdge.get(l);
            Node c2 = nodeByEdge.get(r);

            nodeByEdge.remove(l);
            nodeByEdge.remove(r);

            int pos1 = getPosition(c1);
            int pos2 = getPosition(c2);

            if (pos1 > pos2) {
                int q = pos1;
                pos1 = pos2;
                pos2 = q;
            }
            Node head = getRoot(vertexNode[u]);

            Node[] t1 = split(head, pos2 + 1);
            Node[] t2 = split(t1[0], pos2);
            Node[] t3 = split(t2[0], pos1 + 1);
            Node[] t4 = split(t3[0], pos1);
            merge(t4[0], t1[1]);
        }

        public int getComponentSize(int v) {
            return getRoot(vertexNode[v]).size;
        }

        ArrayList<Integer> spanningEdges;

        public void prepareSpanningEdges() {
            spanningEdges = new ArrayList<>();
            edgeTaken = new HashSet<>();
        }

        public void getSpanningEdges(Node root) {
            if (root == null) {
                return;
            }
            if (!root.hasEdge) {
                return;
            }
            if (root.isHasEdge()) {
                if (!edgeTaken.contains(root.id)) { // It could be put 2 times, direct or inverse
                    edgeTaken.add(root.id);
                    spanningEdges.add(root.id);
                }
            }
            getSpanningEdges(root.l);
            getSpanningEdges(root.r);
        }

        ArrayList<Integer> allEdges;

        public void prepareAllEdges() {
            allEdges = new ArrayList<>();
            edgeTaken = new HashSet<>();
        }

        public int getAllEdges(Node root) {
            if (root == null) {
                return -1;
            }
            if (!root.hasVertex) {
                return -1;
            }
            if (root.isHasVertex()) {
                for (int x : adjacent[root.id][root.level]) {
                    Edge e = edges.get(x);
                    if (forest[level].isConnected(e.u, e.v)) {
                        if (!edgeTaken.contains(x)) {
                            edgeTaken.add(x);
                            allEdges.add(x);
                        }
                    } else {
                        return x;
                    }
                }
            }
            int tmp = getAllEdges(root.l);
            if (tmp != -1) {
                return tmp;
            }
            return getAllEdges(root.r);
        }

        public boolean isConnected(int u, int v) {
            Node r1 = getRoot(vertexNode[u]);
            Node r2 = getRoot(vertexNode[v]);
            return r1 == r2;
        }
    }

    Forest[] forest;
    HashSet<Integer>[][] adjacent;
    HashMap<Integer, Edge> edges; // Edge by id
    HashMap<Edge, Integer> edgeIndex; // id by edge
    HashSet<Integer> edgeTaken; // is the edge was taken into consideration previously
    int curEdge;

    int connected_components = 0;

    public int numberOfCC() {
        return connected_components;
    }

    public boolean isConnected(int u, int v) {
        return forest[0].isConnected(u, v);
    }

    public void addEdge(int u, int v) {
        if (u > v) {
            int q = u;
            u = v;
            v = q;
        }

        Edge e = new Edge(u, v);
        if (edgeIndex.containsKey(e)) { // If the edge exist, do nothing
            return;
        }
        edgeIndex.put(e, curEdge);
        edges.put(curEdge, e);

        if (!forest[0].isConnected(u, v)) { // If this is a spanning tree
            forest[0].link(u, v); // link two forest trees together
            connected_components--;
        } else {
            adjacent[u][0].add(curEdge); // simply add to adjacency list on level 0 and update hasVertex and hasEdge
            adjacent[v][0].add(curEdge);

            forest[0].updateToTop(forest[0].vertexNode[u]);
            forest[0].updateToTop(forest[0].vertexNode[v]);
        }

        curEdge++;
    }

    public void increaseLevel(int x, boolean spanning) {
        Edge edge = edges.get(x);
        int u = edge.u;
        int v = edge.v;
        int level = edge.level;
        edge.level++;
        if (spanning) {
            forest[level].updateToTop(forest[level].nodeByEdge.get(new Edge(u, v)));
            forest[level].updateToTop(forest[level].nodeByEdge.get(new Edge(v, u)));
            forest[level + 1].link(u, v);
        } else {
            adjacent[u][level].remove(x);
            forest[level].updateToTop(forest[level].vertexNode[u]);
            adjacent[v][level].remove(x);
            forest[level].updateToTop(forest[level].vertexNode[v]);

            adjacent[u][level + 1].add(x);
            forest[level + 1].updateToTop(forest[level + 1].vertexNode[u]);
            adjacent[v][level + 1].add(x);
            forest[level + 1].updateToTop(forest[level + 1].vertexNode[v]);
        }
    }

    public void removeEdge(int u, int v) {
        if (u > v) {
            int q = u;
            u = v;
            v = q;
        }
        int id = edgeIndex.get(new Edge(u, v));
        Edge e = edges.get(id);

        int rank = e.level;

        if (!forest[0].nodeByEdge.containsKey(e)) { // The edges is not in the spanning tree
            adjacent[u][rank].remove(id); // simply remove from the adjacency list on level level
            adjacent[v][rank].remove(id);

            forest[rank].updateToTop(forest[rank].vertexNode[u]);
            forest[rank].updateToTop(forest[rank].vertexNode[v]);
            return;
        }

        for (int level = rank; level >= 0; level--) {
            forest[level].cut(u, v);
        }

        boolean replaced = false;
        for (int level = rank; level >= 0; level--) {
            int w = (forest[level].getComponentSize(u) > forest[level].getComponentSize(v))
                    ? v : u; // Choose the smallest component

            forest[level].prepareSpanningEdges();
            forest[level].getSpanningEdges(getRoot(forest[level].vertexNode[w]));
            for (int x : forest[level].spanningEdges) {
                increaseLevel(x, true);
            }

            forest[level].prepareAllEdges();
            int good = forest[level].getAllEdges(getRoot(forest[level].vertexNode[w]));
            for (int x : forest[level].allEdges) {
                increaseLevel(x, false);
            }

            if (good != -1) { // We found good edge
                Edge ge = edges.get(good);

                adjacent[ge.u][level].remove(good);
                adjacent[ge.v][level].remove(good);

                for (int i = level; i >= 0; i--) {
                    forest[i].link(ge.u, ge.v);
                }

                replaced = true;
                break;
            }
        }

        if (!replaced) {
            connected_components++;
        }

        edgeIndex.remove(e);
        edges.remove(id);
    }

    public void setup(int n) {
        connected_components = n;
        int p = 1;
        int k = 1;
        while (p <= n) {
            p *= 2;
            k++;
        }

        forest = new Forest[k];
        for (int i = 0; i < k; i++) {
            forest[i] = new Forest(n, i);
        }

        adjacent = new HashSet[n][k];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < k; j++) {
                adjacent[i][j] = new HashSet<>();
            }
        }

        edgeIndex = new HashMap<>();
        edges = new HashMap<>();
    }

    BufferedReader br;
    StringTokenizer st;

    public String nextToken() throws IOException {
        while (st == null || !st.hasMoreElements()) {
            st = new StringTokenizer(br.readLine());
        }
        return st.nextToken();
    }

    public int nextInt() throws IOException {
        return Integer.parseInt(nextToken());
    }

    public void run() throws IOException {
        br = new BufferedReader(new FileReader("connect.in"));
        PrintWriter out = new PrintWriter("connect.out");

        int n = nextInt();
        int q = nextInt();

        setup(n);

        for (int i = 0; i < q; i++) {
            String query = nextToken();

            if (query.equals("?")) {
                out.println(numberOfCC());
                continue;
            }

            int u = nextInt() - 1;
            int v = nextInt() - 1;

            if (query.equals("c")) {
                out.println(isConnected(u, v));
            } else if (query.equals("+")) {
                addEdge(u, v);
            } else {
                removeEdge(u, v);
            }
        }

        out.close();
    }

    public static void main(String[] args) throws IOException {
        new Solution().run();
    }
}