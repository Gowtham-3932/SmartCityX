package smartcityx;

import java.util.*;

public final class FlowAlgorithms {
    private FlowAlgorithms() {}

    private static class Edge {
        int to, rev, cap;
        Edge(int to, int rev, int cap) { this.to = to; this.rev = rev; this.cap = cap; }
    }

    // Edmonds-Karp maximum flow; O(V E^2). Dinic below is also supplied.
    public static int edmondsKarp(int n, int[][] edges, int source, int sink) {
        List<List<Edge>> g = graph(n, edges);
        int flow = 0;
        while (true) {
            Edge[] parentEdge = new Edge[n];
            int[] parentNode = new int[n]; Arrays.fill(parentNode, -1);
            Queue<Integer> q = new ArrayDeque<>(); q.add(source); parentNode[source] = source;
            while (!q.isEmpty() && parentNode[sink] == -1) {
                int u = q.remove();
                for (Edge e : g.get(u)) if (e.cap > 0 && parentNode[e.to] == -1) {
                    parentNode[e.to] = u; parentEdge[e.to] = e; q.add(e.to);
                }
            }
            if (parentNode[sink] == -1) break;
            int add = Integer.MAX_VALUE;
            for (int v = sink; v != source; v = parentNode[v]) add = Math.min(add, parentEdge[v].cap);
            for (int v = sink; v != source; v = parentNode[v]) {
                Edge e = parentEdge[v]; e.cap -= add; g.get(e.to).get(e.rev).cap += add;
            }
            flow += add;
        }
        return flow;
    }

    public static int dinic(int n, int[][] edges, int source, int sink) {
        List<List<Edge>> g = graph(n, edges);
        int flow = 0;
        while (true) {
            int[] level = new int[n]; Arrays.fill(level, -1);
            Queue<Integer> q = new ArrayDeque<>(); q.add(source); level[source] = 0;
            while (!q.isEmpty()) {
                int u = q.remove();
                for (Edge e : g.get(u)) if (e.cap > 0 && level[e.to] < 0) {
                    level[e.to] = level[u] + 1; q.add(e.to);
                }
            }
            if (level[sink] < 0) return flow;
            int[] it = new int[n];
            int pushed;
            while ((pushed = dfs(source, sink, Integer.MAX_VALUE, g, level, it)) > 0) flow += pushed;
        }
    }

    private static int dfs(int u, int sink, int f, List<List<Edge>> g, int[] level, int[] it) {
        if (u == sink) return f;
        for (; it[u] < g.get(u).size(); it[u]++) {
            Edge e = g.get(u).get(it[u]);
            if (e.cap > 0 && level[e.to] == level[u] + 1) {
                int got = dfs(e.to, sink, Math.min(f, e.cap), g, level, it);
                if (got > 0) { e.cap -= got; g.get(e.to).get(e.rev).cap += got; return got; }
            }
        }
        return 0;
    }

    private static List<List<Edge>> graph(int n, int[][] edges) {
        List<List<Edge>> g = new ArrayList<>();
        for (int i = 0; i < n; i++) g.add(new ArrayList<>());
        for (int[] e : edges) {
            int u=e[0], v=e[1], cap=e[2];
            Edge a = new Edge(v, g.get(v).size(), cap);
            Edge b = new Edge(u, g.get(u).size(), 0);
            g.get(u).add(a); g.get(v).add(b);
        }
        return g;
    }

    // Maximum bipartite matching using DFS augmenting paths. O(VE).
    public static int maximumBipartiteMatching(boolean[][] adj) {
        int left = adj.length, right = left == 0 ? 0 : adj[0].length;
        int[] matchRight = new int[right]; Arrays.fill(matchRight, -1);
        int matches = 0;
        for (int u = 0; u < left; u++) if (augment(u, adj, new boolean[right], matchRight)) matches++;
        return matches;
    }

    private static boolean augment(int u, boolean[][] adj, boolean[] seen, int[] matchRight) {
        for (int v = 0; v < matchRight.length; v++) if (adj[u][v] && !seen[v]) {
            seen[v] = true;
            if (matchRight[v] == -1 || augment(matchRight[v], adj, seen, matchRight)) {
                matchRight[v] = u; return true;
            }
        }
        return false;
    }
}
