package smartcityx;

public class Problem {
    private final int id;
    private final String title;
    private final String description;
    private final String category;
    private final String location;
    private final String reportedAt;
    private String status;
    private String assignedTeam;
    private String solvedAt;

    public Problem(int id, String title, String description, String category,
                   String location, String reportedAt, String status,
                   String assignedTeam, String solvedAt) {
        this.id = id;
        this.title = safe(title);
        this.description = safe(description);
        this.category = safe(category);
        this.location = safe(location);
        this.reportedAt = safe(reportedAt);
        this.status = safe(status);
        this.assignedTeam = safe(assignedTeam);
        this.solvedAt = safe(solvedAt);
    }

    private static String safe(String value) { return value == null ? "" : value.trim(); }
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public String getLocation() { return location; }
    public String getReportedAt() { return reportedAt; }
    public String getStatus() { return status; }
    public String getAssignedTeam() { return assignedTeam; }
    public String getSolvedAt() { return solvedAt; }

    public void setStatus(String status) { this.status = safe(status).toUpperCase(); }
    public void setAssignedTeam(String team) { this.assignedTeam = safe(team); }
    public void setSolvedAt(String time) { this.solvedAt = safe(time); }

    public String getSearchableText() {
        return (title + " " + description + " " + category + " " + location).toLowerCase();
    }

    public String toCsv() {
        return id + "," + csv(title) + "," + csv(description) + "," + csv(category) + ","
                + csv(location) + "," + csv(reportedAt) + "," + csv(status) + ","
                + csv(assignedTeam) + "," + csv(solvedAt);
    }

    private static String csv(String s) {
        String v = s == null ? "" : s;
        return "\"" + v.replace("\"", "\"\"") + "\"";
    }

    public static Problem fromCsv(String line) {
        java.util.List<String> c = parseCsv(line);
        if (c.size() < 9) throw new IllegalArgumentException("Invalid problem CSV row");
        return new Problem(Integer.parseInt(c.get(0)), c.get(1), c.get(2), c.get(3), c.get(4),
                c.get(5), c.get(6), c.get(7), c.get(8));
    }

    private static java.util.List<String> parseCsv(String line) {
        java.util.List<String> out = new java.util.ArrayList<>();
        StringBuilder field = new StringBuilder();
        boolean quoted = false;
        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);
            if (ch == '"') {
                if (quoted && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    field.append('"'); i++;
                } else quoted = !quoted;
            } else if (ch == ',' && !quoted) {
                out.add(field.toString()); field.setLength(0);
            } else field.append(ch);
        }
        out.add(field.toString());
        return out;
    }

    public String displayLine() {
        return String.format("SCX-%-4d | %-22s | %-15s | %-14s | %s",
                id, truncate(title, 22), truncate(location, 15), category, status);
    }

    private static String truncate(String s, int max) {
        return s.length() <= max ? s : s.substring(0, max - 3) + "...";
    }
}
