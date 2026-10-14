package smartcityx;

import java.util.*;

public final class RandomizedParallelAlgorithms {
    private RandomizedParallelAlgorithms() {}

    public static void randomizedQuickSort(int[] a, Random random) {
        quickSort(a, 0, a.length - 1, random);
    }

    private static void quickSort(int[] a, int lo, int hi, Random random) {
        if (lo >= hi) return;
        int pivotIndex = lo + random.nextInt(hi - lo + 1);
        swap(a, pivotIndex, hi);
        int p = partition(a, lo, hi);
        quickSort(a, lo, p - 1, random);
        quickSort(a, p + 1, hi, random);
    }

    private static int partition(int[] a, int lo, int hi) {
        int pivot = a[hi], i = lo;
        for (int j = lo; j < hi; j++) if (a[j] <= pivot) swap(a, i++, j);
        swap(a, i, hi); return i;
    }

    private static void swap(int[] a, int i, int j) { int t=a[i]; a[i]=a[j]; a[j]=t; }

    // Reservoir sampling returns up to k uniformly selected stream items.
    public static <T> List<T> reservoirSample(Iterable<T> stream, int k, Random random) {
        if (k < 0) throw new IllegalArgumentException("k must be non-negative");
        List<T> sample = new ArrayList<>(k);
        long seen = 0;
        for (T item : stream) {
            seen++;
            if (sample.size() < k) sample.add(item);
            else if (k > 0) {
                long j = (long)(random.nextDouble() * seen);
                if (j < k) sample.set((int)j, item);
            }
        }
        return sample;
    }

    // Miller-Rabin probable-prime test using deterministic bases for signed 32-bit positive integers.
    public static boolean millerRabin(int n) {
        if (n < 2) return false;
        for (int p : new int[]{2,3,5,7,11,13,17,19,23,29,31,37}) {
            if (n == p) return true;
            if (n % p == 0) return false;
        }
        int d = n - 1, s = 0;
        while ((d & 1) == 0) { d >>= 1; s++; }
        for (int a : new int[]{2,3,5,7,11}) {
            if (a >= n) continue;
            long x = modPow(a, d, n);
            if (x == 1 || x == n - 1) continue;
            boolean witness = true;
            for (int r = 1; r < s; r++) {
                x = x * x % n;
                if (x == n - 1) { witness = false; break; }
            }
            if (witness) return false;
        }
        return true;
    }

    private static long modPow(long a, int e, int mod) {
        long result = 1, base = a % mod;
        while (e > 0) {
            if ((e & 1) == 1) result = result * base % mod;
            base = base * base % mod; e >>= 1;
        }
        return result;
    }

    public static long[] parallelReduceSum(int[] values) {
        // Java parallel stream reduction; returns {sum, min, max}.
        long sum = Arrays.stream(values).parallel().asLongStream().sum();
        int min = Arrays.stream(values).parallel().min().orElse(0);
        int max = Arrays.stream(values).parallel().max().orElse(0);
        return new long[]{sum, min, max};
    }

    // Blelloch-style inclusive scan using a simple prefix accumulation (O(n)); illustrates scan output.
    public static long[] inclusiveScan(int[] values) {
        long[] out = new long[values.length];
        long sum = 0;
        for (int i = 0; i < values.length; i++) out[i] = (sum += values[i]);
        return out;
    }

    public static String brentTheoremEstimate(int work, int depth, int processors) {
        if (processors < 1) throw new IllegalArgumentException("processors must be >= 1");
        double estimate = Math.ceil((double)work / processors) + depth;
        return String.format(Locale.ROOT, "W=%d, D=%d, P=%d => estimated time <= W/P + D = %.2f units",
                work, depth, processors, estimate);
    }
}
