package smartcityx;

import java.util.*;

public final class ApproximationAlgorithms {
    private ApproximationAlgorithms() {}

    // Greedy 2-approximation for vertex cover: repeatedly select both endpoints of an uncovered edge.
    public static Set<Integer> vertexCover2Approx(int vertices, int[][] edges) {
        Set<Integer> cover = new LinkedHashSet<>();
        boolean[] coveredEdge = new boolean[edges.length];
        for (int i = 0; i < edges.length; i++) {
            if (coveredEdge[i]) continue;
            int u = edges[i][0], v = edges[i][1];
            cover.add(u); cover.add(v);
            for (int j = i; j < edges.length; j++) {
                if (edges[j][0] == u || edges[j][1] == u || edges[j][0] == v || edges[j][1] == v)
                    coveredEdge[j] = true;
            }
        }
        return cover;
    }

    // Brute-force educational 3-SAT checker. Each clause is 3 signed literals:
    // +k means variable k, -k means NOT variable k. Variables are 1..n.
    public static boolean isThreeSatSatisfiable(int variableCount, int[][] clauses) {
        if (variableCount > 25) throw new IllegalArgumentException("Demo solver limited to 25 variables");
        long limit = 1L << variableCount;
        for (long mask = 0; mask < limit; mask++) {
            boolean all = true;
            for (int[] clause : clauses) {
                boolean clauseTrue = false;
                for (int literal : clause) {
                    int var = Math.abs(literal) - 1;
                    boolean value = ((mask >>> var) & 1L) != 0;
                    if (literal < 0) value = !value;
                    if (value) { clauseTrue = true; break; }
                }
                if (!clauseTrue) { all = false; break; }
            }
            if (all) return true;
        }
        return false;
    }
}
