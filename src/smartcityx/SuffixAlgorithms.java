package smartcityx;

import java.util.Arrays;

public final class SuffixAlgorithms {
    private SuffixAlgorithms() {}

    // Straightforward suffix sorting: clear teaching implementation, O(n^2 log n) worst case.
    public static int[] suffixArray(String text) {
        Integer[] idx = new Integer[text.length()];
        for (int i = 0; i < idx.length; i++) idx[i] = i;
        Arrays.sort(idx, (a, b) -> text.substring(a).compareTo(text.substring(b)));
        int[] result = new int[idx.length];
        for (int i = 0; i < idx.length; i++) result[i] = idx[i];
        return result;
    }

    // Kasai LCP: lcp[i] = LCP(sa[i], sa[i+1]); last item is 0.
    public static int[] lcpKasai(String text, int[] sa) {
        int n = text.length();
        int[] rank = new int[n], lcp = new int[n];
        for (int i = 0; i < n; i++) rank[sa[i]] = i;
        int k = 0;
        for (int i = 0; i < n; i++) {
            int r = rank[i];
            if (r == n - 1) { k = 0; continue; }
            int j = sa[r + 1];
            while (i + k < n && j + k < n && text.charAt(i + k) == text.charAt(j + k)) k++;
            lcp[r] = k;
            if (k > 0) k--;
        }
        return lcp;
    }

    public static String longestRepeatedSubstring(String text) {
        if (text.isEmpty()) return "";
        int[] sa = suffixArray(text);
        int[] lcp = lcpKasai(text, sa);
        int best = 0, at = 0;
        for (int i = 0; i < lcp.length; i++) if (lcp[i] > best) { best = lcp[i]; at = sa[i]; }
        return text.substring(at, at + best);
    }
}
