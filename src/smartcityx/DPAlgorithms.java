package smartcityx;

import java.util.*;

public final class DPAlgorithms {
    private DPAlgorithms() {}

    public static int editDistance(String a, String b) {
        a = a.toLowerCase(Locale.ROOT); b = b.toLowerCase(Locale.ROOT);
        int[][] dp = new int[a.length() + 1][b.length() + 1];
        for (int i = 0; i <= a.length(); i++) dp[i][0] = i;
        for (int j = 0; j <= b.length(); j++) dp[0][j] = j;
        for (int i = 1; i <= a.length(); i++) for (int j = 1; j <= b.length(); j++) {
            int cost = a.charAt(i - 1) == b.charAt(j - 1) ? 0 : 1;
            dp[i][j] = Math.min(Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1),
                    dp[i - 1][j - 1] + cost);
        }
        return dp[a.length()][b.length()];
    }

    public static int damerauLevenshtein(String a, String b) {
        a = a.toLowerCase(Locale.ROOT); b = b.toLowerCase(Locale.ROOT);
        int n = a.length(), m = b.length(), inf = n + m;
        int[][] d = new int[n + 2][m + 2];
        d[0][0] = inf;
        for (int i = 0; i <= n; i++) { d[i + 1][0] = inf; d[i + 1][1] = i; }
        for (int j = 0; j <= m; j++) { d[0][j + 1] = inf; d[1][j + 1] = j; }
        Map<Character, Integer> last = new HashMap<>();
        for (int i = 1; i <= n; i++) {
            int db = 0;
            for (int j = 1; j <= m; j++) {
                int i1 = last.getOrDefault(b.charAt(j - 1), 0);
                int j1 = db;
                int cost = 1;
                if (a.charAt(i - 1) == b.charAt(j - 1)) { cost = 0; db = j; }
                d[i + 1][j + 1] = Math.min(Math.min(d[i][j] + cost,
                        d[i + 1][j] + 1), Math.min(d[i][j + 1] + 1,
                        d[i1][j1] + (i - i1 - 1) + 1 + (j - j1 - 1)));
            }
            last.put(a.charAt(i - 1), i);
        }
        return d[n + 1][m + 1];
    }

    // Knapsack-style subset DP: choose resources with maximum utility under budget.
    public static List<Integer> resourceSelection(int[] costs, int[] utility, int budget) {
        int n = costs.length;
        if (n > 20) throw new IllegalArgumentException("Bitmask DP supports at most 20 resources");
        int states = 1 << n;
        int bestMask = 0, bestUtility = 0;
        for (int mask = 0; mask < states; mask++) {
            int cost = 0, value = 0;
            for (int i = 0; i < n; i++) if ((mask & (1 << i)) != 0) {
                cost += costs[i]; value += utility[i];
            }
            if (cost <= budget && value > bestUtility) { bestUtility = value; bestMask = mask; }
        }
        List<Integer> chosen = new ArrayList<>();
        for (int i = 0; i < n; i++) if ((bestMask & (1 << i)) != 0) chosen.add(i);
        return chosen;
    }

    // Matrix-chain minimum multiplication count. dimensions: A_i = dims[i-1] x dims[i].
    public static long matrixChainCost(int[] dims) {
        int n = dims.length - 1;
        if (n <= 1) return 0;
        long[][] dp = new long[n][n];
        for (int len = 2; len <= n; len++) for (int i = 0; i + len - 1 < n; i++) {
            int j = i + len - 1; dp[i][j] = Long.MAX_VALUE;
            for (int k = i; k < j; k++) {
                long cost = dp[i][k] + dp[k + 1][j] + (long)dims[i] * dims[k + 1] * dims[j + 1];
                dp[i][j] = Math.min(dp[i][j], cost);
            }
        }
        return dp[0][n - 1];
    }
}
