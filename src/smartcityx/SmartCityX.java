package smartcityx;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class SmartCityX {
    private static final Scanner IN = new Scanner(System.in);
    private static ProblemRepository repo;
    private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static void main(String[] args) {
        try {
            Path dataPath = args.length > 0 ? Path.of(args[0]) : Path.of("data", "problems.csv");
            repo = new ProblemRepository(dataPath);
            System.out.println("SmartCityX data file: " + dataPath.toAbsolutePath());
            runMenu();
        } catch (IOException e) {
            System.err.println("Could not load/save problem data: " + e.getMessage());
        }
    }

    private static void runMenu() {
        while (true) {
            System.out.println("\n==============================================================");
            System.out.println("                 SMARTCITYX");
            System.out.println("       SMART CITY PROBLEM MANAGEMENT PLATFORM");
            System.out.println("==============================================================");
            System.out.println("1.  Citizen Report Problem");
            System.out.println("2.  Admin Dashboard");
            System.out.println("3.  View All Problems");
            System.out.println("4.  Update Problem Status");
            System.out.println("5.  Search Problems (KMP / Rabin-Karp)");
            System.out.println("6.  Detect Emergency Keywords (Aho-Corasick)");
            System.out.println("7.  Find Similar City Reports (Suffix Array / LCP)");
            System.out.println("8.  Correct Citizen Query (Edit Distance)");
            System.out.println("9.  City Resource Selection (Bitmask DP)");
            System.out.println("10. Network Flow & Resource Matching (CO4)");
            System.out.println("11. Infrastructure Conflict Coverage (CO5)");
            System.out.println("12. Randomized / Parallel Analytics (CO6)");
            System.out.println("13. Search by Location");
            System.out.println("14. Search by Category");
            System.out.println("15. Run Algorithm Test Suite");
            System.out.println("0.  Exit");
            System.out.println("--------------------------------------------------------------");
            int choice = readInt("Enter choice: ");
            try {
                switch (choice) {
                    case 1 -> reportProblem();
                    case 2 -> dashboard();
                    case 3 -> viewAll();
                    case 4 -> updateStatus();
                    case 5 -> searchProblems();
                    case 6 -> emergencyKeywords();
                    case 7 -> similarReports();
                    case 8 -> correctQuery();
                    case 9 -> resourceSelection();
                    case 10 -> flowDemo();
                    case 11 -> approximationDemo();
                    case 12 -> randomizedParallelDemo();
                    case 13 -> searchLocation();
                    case 14 -> searchCategory();
                    case 15 -> TestSuite.runAll();
                    case 0 -> { repo.save(); System.out.println("Data saved. Goodbye from SmartCityX!"); return; }
                    default -> System.out.println("Invalid menu choice.");
                }
            } catch (IOException | IllegalArgumentException e) {
                System.out.println("Operation failed: " + e.getMessage());
            }
        }
    }

    private static String readLine(String prompt) {
        System.out.print(prompt);
        return IN.nextLine().trim();
    }
    private static int readInt(String prompt) {
        while (true) {
            String s = readLine(prompt);
            try { return Integer.parseInt(s); }
            catch (NumberFormatException e) { System.out.println("Enter a valid number."); }
        }
    }
    private static String now() { return LocalDateTime.now().format(TIME); }
    private static String readProblemId() {
        String raw = readLine("Enter Problem ID (e.g., SCX-1021 or 1021): ").toUpperCase(Locale.ROOT);
        if (raw.startsWith("SCX-")) raw = raw.substring(4);
        try { return Integer.toString(Integer.parseInt(raw)); }
        catch (NumberFormatException e) { System.out.println("Invalid ID format."); return ""; }
    }

    private static void reportProblem() throws IOException {
        System.out.println("\n--- CITIZEN REPORT ---");
        String title = readLine("Problem title: ");
        String desc = readLine("Description: ");
        String category = readLine("Category (Roads/Water/Electricity/Garbage/Traffic/Emergency/Other): ");
        String location = readLine("City/area location: ");
        if (title.isBlank() || category.isBlank() || location.isBlank()) {
            System.out.println("Title, category, and location are required."); return;
        }
        Problem p = repo.add(title, desc, category, location, now());
        System.out.println("Report submitted successfully.");
        System.out.println("Problem ID: SCX-" + p.getId() + " | Status: NEW");
        System.out.println("Saved to CSV.");
    }

    private static void dashboard() {
        Map<String, Integer> statuses = new LinkedHashMap<>();
        statuses.put("NEW", 0); statuses.put("PROGRESS", 0); statuses.put("SOLVED", 0);
        Map<String, Integer> categories = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        for (Problem p : repo.all()) {
            statuses.put(p.getStatus(), statuses.getOrDefault(p.getStatus(), 0) + 1);
            categories.put(p.getCategory(), categories.getOrDefault(p.getCategory(), 0) + 1);
        }
        System.out.println("\n=================== SMARTCITYX ADMIN ===================");
        System.out.printf("TOTAL: %-5d NEW: %-5d PROGRESS: %-5d SOLVED: %-5d%n",
                repo.all().size(), statuses.get("NEW"), statuses.get("PROGRESS"), statuses.get("SOLVED"));
        System.out.println("\nPROBLEMS BY CATEGORY");
        categories.forEach((k,v) -> System.out.printf("  %-22s %d%n", k, v));
        System.out.println("\nLIVE PROBLEM FEED (latest first)");
        List<Problem> all = repo.all();
        for (int i = all.size() - 1, count = 0; i >= 0 && count < 6; i--, count++)
            System.out.println(all.get(i).displayLine());
        System.out.println("=========================================================");
    }

    private static void viewAll() {
        System.out.println("\nID       | TITLE                  | LOCATION        | CATEGORY       | STATUS");
        System.out.println("---------+------------------------+-----------------+----------------+----------");
        for (int i = repo.all().size() - 1; i >= 0; i--) System.out.println(repo.all().get(i).displayLine());
        if (repo.all().isEmpty()) System.out.println("No reports yet.");
    }

    private static void updateStatus() throws IOException {
        String idText = readProblemId();
        if (idText.isEmpty()) return;
        Problem p = repo.findById(Integer.parseInt(idText));
        if (p == null) { System.out.println("Problem not found."); return; }
        System.out.println(p.displayLine());
        System.out.println("1. NEW  2. PROGRESS  3. SOLVED");
        int c = readInt("Select status: ");
        String status = switch (c) { case 1 -> "NEW"; case 2 -> "PROGRESS"; case 3 -> "SOLVED"; default -> ""; };
        if (status.isEmpty()) { System.out.println("Invalid status."); return; }
        String team = readLine("Assigned/resolving team (or leave blank to keep current): ");
        String solved = status.equals("SOLVED") ? readLine("Solved time (blank = current time): ") : "-";
        if (solved.isBlank() && status.equals("SOLVED")) solved = now();
        repo.updateStatus(p.getId(), status, team, solved);
        System.out.println("Updated SCX-" + p.getId() + " to " + status + ". CSV saved.");
    }

    private static void searchProblems() {
        String key = readLine("Enter search keyword: ");
        if (key.isBlank()) { System.out.println("Keyword cannot be blank."); return; }
        int count = 0;
        for (Problem p : repo.all()) {
            String text = p.getSearchableText();
            if (StringAlgorithms.kmpContains(text, key) || StringAlgorithms.rabinKarpContains(text, key)) {
                System.out.println(p.displayLine()); count++;
            }
        }
        System.out.println("Matches: " + count + " (KMP and Rabin-Karp).");
        int[] z = StringAlgorithms.zFunction(key + "$" + key);
        System.out.println("Z-function demonstration completed; array length = " + z.length);
    }

    private static void emergencyKeywords() {
        String report = readLine("Enter report text: ");
        Set<String> words = StringAlgorithms.ahoCorasick(report,
                List.of("fire", "flood", "accident", "leakage", "emergency", "danger",
                        "electricity", "gas leak", "water overflow", "road blocked"));
        if (words.isEmpty()) System.out.println("No configured emergency keywords detected.");
        else System.out.println("Detected keywords: " + words);
        System.out.println("Note: keyword detection is a triage aid, not a substitute for emergency services.");
    }

    private static void similarReports() {
        String text = readLine("Enter combined report text to analyze: ").toLowerCase(Locale.ROOT);
        if (text.isEmpty()) { System.out.println("Text cannot be empty."); return; }
        int[] sa = SuffixAlgorithms.suffixArray(text);
        int[] lcp = SuffixAlgorithms.lcpKasai(text, sa);
        String repeated = SuffixAlgorithms.longestRepeatedSubstring(text);
        System.out.println("Suffix Array: " + Arrays.toString(sa));
        System.out.println("LCP/Kasai:    " + Arrays.toString(lcp));
        System.out.println("Longest repeated substring: " + (repeated.isEmpty() ? "(none)" : repeated));
        System.out.println("This basic suffix-array builder is for demonstration; see README complexity notes.");
    }

    private static void correctQuery() {
        String query = readLine("Enter possibly misspelled keyword: ");
        List<String> dictionary = List.of("water", "leakage", "pothole", "road", "traffic", "garbage",
                "electricity", "emergency", "fire", "flood", "drainage", "streetlight", "accident");
        String best = ""; int distance = Integer.MAX_VALUE;
        for (String word : dictionary) {
            int d = DPAlgorithms.editDistance(query, word);
            if (d < distance) { distance = d; best = word; }
        }
        System.out.println("Suggested keyword: " + best + " (Levenshtein distance = " + distance + ")");
        System.out.println("Damerau-Levenshtein example (\"waetr\" -> \"water\"): "
                + DPAlgorithms.damerauLevenshtein("waetr", "water"));
    }

    private static void resourceSelection() {
        String[] names = {"Ambulance", "Fire truck", "Water tanker", "Repair team", "Traffic crew"};
        int[] costs = {10, 15, 8, 12, 5};
        int[] utilities = {10, 12, 7, 8, 4};
        int budget = 25;
        List<Integer> chosen = DPAlgorithms.resourceSelection(costs, utilities, budget);
        int cost = 0, utility = 0;
        System.out.println("Budget = " + budget + " resource units");
        for (int i : chosen) { System.out.println("- " + names[i] + " (cost " + costs[i] + ", utility " + utilities[i] + ")");
            cost += costs[i]; utility += utilities[i]; }
        System.out.println("Total cost = " + cost + ", total utility = " + utility);
        System.out.println("Matrix-chain sample minimum multiplications = "
                + DPAlgorithms.matrixChainCost(new int[]{10, 30, 5, 60}));
    }

    private static void flowDemo() {
        int[][] edges = {{0,1,10},{0,2,8},{1,2,5},{1,3,5},{2,3,10}};
        int ek = FlowAlgorithms.edmondsKarp(4, edges, 0, 3);
        int dinic = FlowAlgorithms.dinic(4, edges, 0, 3);
        boolean[][] match = {{true,true,false},{true,false,true},{false,true,true}};
        System.out.println("City network max flow (Edmonds-Karp): " + ek);
        System.out.println("City network max flow (Dinic):        " + dinic);
        System.out.println("Emergency team/resource matching size: " + FlowAlgorithms.maximumBipartiteMatching(match));
        System.out.println("Interpretation: capacities are illustrative units, not live infrastructure measurements.");
    }

    private static void approximationDemo() {
        int[][] conflicts = {{0,1},{1,2},{2,3},{3,4},{0,4}};
        Set<Integer> cover = ApproximationAlgorithms.vertexCover2Approx(5, conflicts);
        System.out.println("Infrastructure conflict edges: " + Arrays.deepToString(conflicts));
        System.out.println("2-approximate vertex cover nodes: " + cover);
        int[][] clauses = {{1,-2,3},{-1,2,3}};
        System.out.println("Sample 3-SAT satisfiable: " + ApproximationAlgorithms.isThreeSatSatisfiable(3, clauses));
        System.out.println("3-SAT exhaustive checking is exponential; use small examples only.");
    }

    private static void randomizedParallelDemo() {
        int[] priorities = {8, 3, 11, 1, 7, 3, 15, 4};
        RandomizedParallelAlgorithms.randomizedQuickSort(priorities, new Random(42));
        System.out.println("Randomized QuickSort priorities: " + Arrays.toString(priorities));
        System.out.println("Reservoir sample of report IDs: "
                + RandomizedParallelAlgorithms.reservoirSample(
                        repo.all().stream().map(p -> "SCX-" + p.getId()).toList(), 3, new Random(42)));
        int[] measurements = {12, 4, 7, 3, 9};
        System.out.println("Inclusive scan: " + Arrays.toString(RandomizedParallelAlgorithms.inclusiveScan(measurements)));
        System.out.println("Parallel reduce {sum,min,max}: "
                + Arrays.toString(RandomizedParallelAlgorithms.parallelReduceSum(measurements)));
        System.out.println(RandomizedParallelAlgorithms.brentTheoremEstimate(1000, 40, 8));
        System.out.println("Miller-Rabin: 104729 prime? " + RandomizedParallelAlgorithms.millerRabin(104729));
    }

    private static void searchLocation() {
        String location = readLine("Enter location keyword: ").toLowerCase(Locale.ROOT);
        int n = 0;
        for (Problem p : repo.all()) if (p.getLocation().toLowerCase(Locale.ROOT).contains(location)) {
            System.out.println(p.displayLine()); n++;
        }
        System.out.println("Location matches: " + n);
    }

    private static void searchCategory() {
        String category = readLine("Enter category: ").toLowerCase(Locale.ROOT);
        int n = 0;
        for (Problem p : repo.all()) if (p.getCategory().equalsIgnoreCase(category)) {
            System.out.println(p.displayLine()); n++;
        }
        System.out.println("Category matches: " + n);
    }
}
