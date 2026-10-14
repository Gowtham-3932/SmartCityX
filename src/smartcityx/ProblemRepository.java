package smartcityx;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class ProblemRepository {
    private final Path file;
    private final List<Problem> problems = new ArrayList<>();
    private int nextId = 1021;

    public ProblemRepository(Path file) throws IOException {
        this.file = file;
        Path parent = file.getParent();
        if (parent != null) Files.createDirectories(parent);
        if (Files.exists(file)) load();
        if (problems.isEmpty()) {
            addInitialSamples();
            save();
        }
        for (Problem p : problems) nextId = Math.max(nextId, p.getId() + 1);
    }

    public List<Problem> all() { return Collections.unmodifiableList(problems); }

    public Problem findById(int id) {
        for (Problem p : problems) if (p.getId() == id) return p;
        return null;
    }

    public Problem add(String title, String description, String category, String location, String time)
            throws IOException {
        Problem p = new Problem(nextId++, title, description, category, location, time,
                "NEW", "Not assigned", "-");
        problems.add(p);
        save();
        return p;
    }

    public boolean updateStatus(int id, String status, String team, String solvedAt) throws IOException {
        Problem p = findById(id);
        if (p == null) return false;
        p.setStatus(status);
        if (team != null && !team.isBlank()) p.setAssignedTeam(team);
        if ("SOLVED".equals(status)) p.setSolvedAt(solvedAt == null ? "" : solvedAt);
        else p.setSolvedAt("-");
        save();
        return true;
    }

    public void save() throws IOException {
        List<String> lines = new ArrayList<>();
        lines.add("id,title,description,category,location,reportedAt,status,assignedTeam,solvedAt");
        for (Problem p : problems) lines.add(p.toCsv());
        Files.write(file, lines, StandardCharsets.UTF_8, StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
    }

    private void load() throws IOException {
        List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
        for (int i = 1; i < lines.size(); i++) {
            if (lines.get(i).isBlank()) continue;
            try { problems.add(Problem.fromCsv(lines.get(i))); }
            catch (RuntimeException ex) {
                System.err.println("Skipping invalid data row " + (i + 1) + ": " + ex.getMessage());
            }
        }
    }

    private void addInitialSamples() {
        problems.add(new Problem(1021, "Water leakage", "Pipe leaking beside main road",
                "Water", "MVP", "2026-10-14 09:10", "NEW", "Not assigned", "-"));
        problems.add(new Problem(1020, "Pothole", "Large pothole near bus stop",
                "Roads", "Gajuwaka", "2026-10-14 09:20", "PROGRESS", "Road Maintenance Team", "-"));
        problems.add(new Problem(1019, "Street light", "Street light is not working",
                "Electricity", "NAD", "2026-10-14 08:40", "SOLVED", "Electrical Team", "2026-10-14 10:00"));
        problems.add(new Problem(1018, "Garbage overflow", "Garbage bins are full",
                "Garbage", "Madhurawada", "2026-10-14 08:15", "NEW", "Not assigned", "-"));
        problems.add(new Problem(1017, "Traffic signal failure", "Signal not working at junction",
                "Traffic", "MVP", "2026-10-14 07:55", "PROGRESS", "Traffic Team", "-"));
        problems.add(new Problem(1016, "Fire emergency", "Fire reported near market",
                "Emergency", "Gajuwaka", "2026-10-14 07:30", "SOLVED", "Fire Service", "2026-10-14 07:50"));
    }
}
