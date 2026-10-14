package smartcityx;

import java.util.*;

public final class StringAlgorithms {
    private StringAlgorithms() {}

    public static boolean kmpContains(String text, String pattern) {
        if (pattern == null || pattern.isEmpty()) return true;
        text = text.toLowerCase(Locale.ROOT);
        pattern = pattern.toLowerCase(Locale.ROOT);
        int[] lps = lps(pattern);
        int i = 0, j = 0;
        while (i < text.length()) {
            if (text.charAt(i) == pattern.charAt(j)) {
                i++; j++;
                if (j == pattern.length()) return true;
            } else if (j > 0) j = lps[j - 1];
            else i++;
        }
        return false;
    }

    private static int[] lps(String p) {
        int[] lps = new int[p.length()];
        for (int i = 1, len = 0; i < p.length();) {
            if (p.charAt(i) == p.charAt(len)) lps[i++] = ++len;
            else if (len > 0) len = lps[len - 1];
            else lps[i++] = 0;
        }
        return lps;
    }

    public static int[] zFunction(String s) {
        int n = s.length();
        int[] z = new int[n];
        for (int i = 1, left = 0, right = 0; i < n; i++) {
            if (i <= right) z[i] = Math.min(right - i + 1, z[i - left]);
            while (i + z[i] < n && s.charAt(z[i]) == s.charAt(i + z[i])) z[i]++;
            if (i + z[i] - 1 > right) { left = i; right = i + z[i] - 1; }
        }
        return z;
    }

    public static boolean rabinKarpContains(String text, String pattern) {
        text = text.toLowerCase(Locale.ROOT);
        pattern = pattern.toLowerCase(Locale.ROOT);
        int n = text.length(), m = pattern.length();
        if (m == 0) return true;
        if (m > n) return false;
        final long base = 257, mod = 1_000_000_007L;
        long power = 1, ph = 0, th = 0;
        for (int i = 1; i < m; i++) power = power * base % mod;
        for (int i = 0; i < m; i++) {
            ph = (ph * base + pattern.charAt(i)) % mod;
            th = (th * base + text.charAt(i)) % mod;
        }
        for (int i = 0; i <= n - m; i++) {
            if (ph == th && text.regionMatches(i, pattern, 0, m)) return true;
            if (i < n - m) {
                th = (th - text.charAt(i) * power % mod + mod) % mod;
                th = (th * base + text.charAt(i + m)) % mod;
            }
        }
        return false;
    }

    public static Set<String> ahoCorasick(String text, List<String> keywords) {
        class Node {
            Map<Character, Node> next = new HashMap<>();
            Node fail;
            Set<String> out = new LinkedHashSet<>();
        }
        Node root = new Node();
        for (String word : keywords) {
            if (word == null || word.trim().isEmpty()) continue;
            Node cur = root;
            String w = word.toLowerCase(Locale.ROOT);
            for (char c : w.toCharArray()) cur = cur.next.computeIfAbsent(c, k -> new Node());
            cur.out.add(w);
        }
        Queue<Node> q = new ArrayDeque<>();
        root.fail = root;
        for (Node child : root.next.values()) { child.fail = root; q.add(child); }
        while (!q.isEmpty()) {
            Node cur = q.remove();
            for (Map.Entry<Character, Node> e : cur.next.entrySet()) {
                char c = e.getKey(); Node child = e.getValue(); Node f = cur.fail;
                while (f != root && !f.next.containsKey(c)) f = f.fail;
                if (f.next.containsKey(c) && f.next.get(c) != child) child.fail = f.next.get(c);
                else child.fail = root;
                child.out.addAll(child.fail.out); q.add(child);
            }
        }
        Set<String> found = new LinkedHashSet<>();
        Node cur = root;
        for (char c : text.toLowerCase(Locale.ROOT).toCharArray()) {
            while (cur != root && !cur.next.containsKey(c)) cur = cur.fail;
            cur = cur.next.getOrDefault(c, root);
            found.addAll(cur.out);
        }
        return found;
    }
}
