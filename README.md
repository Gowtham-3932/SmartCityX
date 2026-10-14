# SmartCityX — DSA-Based Smart City Problem Management Platform

A Java console project demonstrating DSA modules CO1–CO6 through city complaint management, keyword search, report similarity, resource selection, network flow, infrastructure conflict coverage, and randomized/parallel analytics.

## Features

- Citizen complaint submission with IDs such as `SCX-1021`
- Admin dashboard with status counts, category totals, and recent reports
- Update problem status: `NEW`, `PROGRESS`, `SOLVED`
- Persistent CSV storage (`data/problems.csv`)
- KMP and Rabin–Karp search; Z-function demonstration; Aho–Corasick multi-keyword detection
- Suffix Array and Kasai LCP demonstration
- Levenshtein and Damerau–Levenshtein distance
- Bitmask resource selection and Matrix-Chain DP
- Edmonds–Karp, Dinic maximum flow, and bipartite matching
- 3-SAT brute-force demonstration and Vertex Cover 2-approximation
- Randomized QuickSort, reservoir sampling, Miller–Rabin, prefix scan, parallel reduction, Brent theorem estimate
- Built-in algorithm test suite

## Requirements

- JDK 17 or newer (uses switch expressions and `Stream.toList()`)
- Eclipse IDE or terminal

## Run in Eclipse

1. Create a Java project named `SmartCityX`.
2. Use JDK 17 or newer.
3. Create package `smartcityx` under `src`.
4. Copy all `.java` files from `src/smartcityx/` into that package.
5. In project root, create a folder named `data` (the application also creates it if needed).
6. Run `SmartCityX.java` as **Java Application**.
7. The CSV is saved to `data/problems.csv`, relative to the Eclipse working directory.

## Run from terminal

From the project root:

```bash
javac -d out src/smartcityx/*.java
java -cp out smartcityx.SmartCityX
```

To select a different CSV path:

```bash
java -cp out smartcityx.SmartCityX data/problems.csv
```

## Example output

```text
SmartCityX data file: .../data/problems.csv

==============================================================
                 SMARTCITYX
       SMART CITY PROBLEM MANAGEMENT PLATFORM
==============================================================
1.  Citizen Report Problem
2.  Admin Dashboard
3.  View All Problems
4.  Update Problem Status
5.  Search Problems (KMP / Rabin-Karp)
6.  Detect Emergency Keywords (Aho-Corasick)
7.  Find Similar City Reports (Suffix Array / LCP)
8.  Correct Citizen Query (Edit Distance)
9.  City Resource Selection (Bitmask DP)
10. Network Flow & Resource Matching (CO4)
11. Infrastructure Conflict Coverage (CO5)
12. Randomized / Parallel Analytics (CO6)
13. Search by Location
14. Search by Category
15. Run Algorithm Test Suite
0.  Exit

Enter choice: 1

--- CITIZEN REPORT ---
Problem title: Water leakage
Description: Pipe leaking near the school
Category (Roads/Water/Electricity/Garbage/Traffic/Emergency/Other): Water
City/area location: MVP
Report submitted successfully.
Problem ID: SCX-1022 | Status: NEW
Saved to CSV.

Enter choice: 2

=================== SMARTCITYX ADMIN ===================
TOTAL: 7     NEW: 3     PROGRESS: 2     SOLVED: 2

PROBLEMS BY CATEGORY
  Electricity            1
  Emergency              1
  Garbage                1
  Roads                  1
  Traffic                1
  Water                  2

LIVE PROBLEM FEED (latest first)
SCX-1022 | Water leakage          | MVP             | Water          | NEW
...
```

Exact counts depend on the reports currently saved in your CSV file. The initial sample data is loaded only when the CSV is absent or empty.

## Menu demonstrations

### Search complaint
```text
Enter choice: 5
Enter search keyword: water
SCX-1021 | Water leakage | MVP | Water | NEW
Matches: 1 (KMP and Rabin-Karp).
```

### Emergency keyword detection
```text
Enter choice: 6
Enter report text: fire accident near water pipeline
Detected keywords: [fire, accident, water]
```

### Correct a citizen query
```text
Enter choice: 8
Enter possibly misspelled keyword: pothol
Suggested keyword: pothole (Levenshtein distance = 1)
```

### Update status
```text
Enter choice: 4
Enter Problem ID (e.g., SCX-1021 or 1021): SCX-1021
1. NEW  2. PROGRESS  3. SOLVED
Select status: 3
Assigned/resolving team (or leave blank to keep current): Water Maintenance Team
Solved time (blank = current time): 
Updated SCX-1021 to SOLVED. CSV saved.
```

### Test suite
Select `15`. Expected final line:
```text
Algorithm tests: 12/12 passed.
```

## DSA mapping and complexity

| CO | Algorithms | Use | Complexity |
|---|---|---|---|
| CO1 | KMP | Search one pattern | O(n+m) time, O(m) space |
| CO1 | Rabin–Karp | Rolling-hash keyword search | Average O(n+m), worst O(nm) |
| CO1 | Z-function | Prefix matching / pattern analysis | O(n) time, O(n) space |
| CO1 | Aho–Corasick | Multiple keywords in one report | O(total pattern length + text length + matches) |
| CO2 | Suffix Array (teaching implementation) | Suffix ordering | O(n² log n) worst-case due to substring comparisons |
| CO2 | Kasai LCP | Common prefixes between adjacent suffixes | O(n) time, O(n) space |
| CO3 | Levenshtein / Damerau–Levenshtein | Query correction | O(nm) time and space |
| CO3 | Bitmask subset enumeration | Resource choice | O(2^n n) time, O(n) extra space in this implementation |
| CO3 | Matrix-Chain DP | Analytics operation order | O(n³) time, O(n²) space |
| CO4 | Edmonds–Karp | Network capacity | O(VE²) |
| CO4 | Dinic | Maximum flow | General bound O(V²E); often faster on practical networks |
| CO4 | Bipartite matching | Match teams to jobs | O(VE) for this DFS augmenting-path version |
| CO5 | 3-SAT brute force | Constraint satisfiability demo | O(2^n × number of clauses) |
| CO5 | Vertex Cover 2-approximation | Infrastructure conflict coverage | O(V+E) for typical adjacency/edge processing; this edge-list implementation is O(E²) |
| CO6 | Randomized QuickSort | Rank priorities | Expected O(n log n), worst O(n²) |
| CO6 | Reservoir Sampling | Sample a stream | O(N) time, O(k) space |
| CO6 | Miller–Rabin | Primality check | O(log n) modular multiplications per base for fixed-width integers |
| CO6 | Parallel Reduce | City statistics | Depends on input size, processors, and runtime overhead |
| CO6 | Inclusive scan | Cumulative sensor values | O(n) in this serial reference implementation |
| CO6 | Brent theorem estimate | Parallel-work estimate | O(1) calculation |

## Important limitations / academic honesty

- This is a runnable **educational prototype**, not a live connection to city sensors or municipal systems.
- The sample network capacities and resource utilities are illustrative.
- The suffix-array builder uses straightforward suffix sorting, not SA-IS. SA-IS and a true suffix tree/automaton are not included in this version.
- The scan function demonstrates inclusive prefix-sum results serially; it is not a parallel Blelloch scan implementation.
- 3-SAT is demonstrated by exhaustive checking on small inputs, not a scalable SAT solver.
- CSV persistence is local to the machine. For multi-user production use, add a database, authentication, authorization, audit logs, and API service.
- Do not claim all listed advanced variants are implemented if your evaluator specifically asks for SA-IS, Ford–Fulkerson, or a true parallel scan. Implement and test those variants separately if they are required by your course.

## GitHub setup

```bash
git init
git add .
git commit -m "Initial SmartCityX DSA platform"
git branch -M main
git remote add origin YOUR_GITHUB_REPOSITORY_URL
git push -u origin main
```

Use meaningful commits as you add and test modules; do not create fake development history. Add screenshots and your actual test results to the README.
