package smartcityx;

import java.util.*;

public final class TestSuite {
    private TestSuite() {}

    public static void main(String[] args) {
        runAll();
    }

    public static void runAll() {
        int passed = 0, total = 0;
        total++; if (StringAlgorithms.kmpContains("water pipeline leakage", "leakage")) passed++; else fail("KMP");
        total++; if (StringAlgorithms.rabinKarpContains("street light failure", "light")) passed++; else fail("Rabin-Karp");
        total++; if (StringAlgorithms.ahoCorasick("fire accident", Arrays.asList("fire", "accident")).size() == 2) passed++; else fail("Aho-Corasick");
        total++; if (DPAlgorithms.editDistance("pothol", "pothole") == 1) passed++; else fail("Edit distance");
        total++; if (DPAlgorithms.damerauLevenshtein("waetr", "water") == 1) passed++; else fail("Damerau-Levenshtein");
        total++; if (SuffixAlgorithms.longestRepeatedSubstring("banana").equals("ana")) passed++; else fail("Suffix/LCP");
        total++; if (FlowAlgorithms.edmondsKarp(2, new int[][]{{0,1,7}}, 0, 1) == 7) passed++; else fail("Edmonds-Karp");
        total++; if (FlowAlgorithms.dinic(2, new int[][]{{0,1,7}}, 0, 1) == 7) passed++; else fail("Dinic");
        total++; if (FlowAlgorithms.maximumBipartiteMatching(new boolean[][]{{true,false},{false,true}}) == 2) passed++; else fail("Matching");
        total++; if (ApproximationAlgorithms.isThreeSatSatisfiable(1, new int[][]{{1,1,1}})) passed++; else fail("3-SAT");
        total++; if (RandomizedParallelAlgorithms.millerRabin(104729)) passed++; else fail("Miller-Rabin");
        int[] arr = {4,1,3,2}; RandomizedParallelAlgorithms.randomizedQuickSort(arr, new Random(1));
        total++; if (Arrays.equals(arr, new int[]{1,2,3,4})) passed++; else fail("Randomized QuickSort");
        System.out.println("\nAlgorithm tests: " + passed + "/" + total + " passed.");
    }

    private static void fail(String name) { System.out.println("FAILED: " + name); }
}
