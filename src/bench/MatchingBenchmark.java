package bench;

import matching.KMPMatcher;
import matching.NaiveMatcher;
import matching.RabinKarpMatcher;
import matching.StringMatcher;
import util.StringGenerator;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class MatchingBenchmark {
    private static final long SEED = 42L;
    private static final int[] TEXT_SIZES = {10_000, 50_000, 100_000};
    private static final int PATTERN_LEN = 20;
    private static final int RUNS = 5;

    private static volatile int sink;

    private record Scenario(String name, String text, String pattern) {}

    public static void main(String[] args) throws IOException {
        StringMatcher[] matchers = {new NaiveMatcher(), new KMPMatcher(), new RabinKarpMatcher()};

        List<Scenario> scenarios = buildScenarios();
        warmUp(matchers);

        List<String> csvRows = new ArrayList<>();
        System.out.printf("%-10s %8s %-18s %12s %14s %8s%n",
                "scenario", "n", "algorithm", "time (ms)", "comparisons", "matches");

        for (Scenario s : scenarios) {
            List<Integer> reference = null;
            for (StringMatcher m : matchers) {
                double ms = averageMillis(m, s);

                List<Integer> res = m.search(s.text(), s.pattern());
                long comparisons = m.getComparisons();
                long spurious = (m instanceof RabinKarpMatcher rk) ? rk.getSpuriousHits() : 0;

                if (reference == null) {
                    reference = res;
                } else if (!reference.equals(res)) {
                    throw new IllegalStateException("Result mismatch in scenario " + s.name()
                            + " for " + m.getClass().getSimpleName());
                }

                String algo = m.getClass().getSimpleName().replace("Matcher", "");
                System.out.printf(Locale.ROOT, "%-10s %8d %-18s %12.4f %14d %8d%n",
                        s.name(), s.text().length(), algo, ms, comparisons, res.size());
                csvRows.add(String.format(Locale.ROOT, "%s,%d,%d,%s,%.4f,%d,%d,%d",
                        s.name(), s.text().length(), s.pattern().length(), algo,
                        ms, comparisons, res.size(), spurious));
            }
        }

        writeCsv(Paths.get("results", "tables", "matching_results.csv"), csvRows);
    }

    private static List<Scenario> buildScenarios() {
        Random rnd = new Random(SEED);
        List<Scenario> list = new ArrayList<>();
        for (int n : TEXT_SIZES) {
            String text = StringGenerator.random(n, rnd);
            String present = text.substring(n / 2, n / 2 + PATTERN_LEN);
            String absent = makeAbsentPattern(text, rnd);
            list.add(new Scenario("present", text, present));
            list.add(new Scenario("absent", text, absent));

            String worstText = "A".repeat(n);
            String worstPattern = "A".repeat(PATTERN_LEN - 1) + "B";
            list.add(new Scenario("worst-case", worstText, worstPattern));
        }
        return list;
    }

    private static String makeAbsentPattern(String text, Random rnd) {
        NaiveMatcher check = new NaiveMatcher();
        while (true) {
            String p = StringGenerator.random(PATTERN_LEN, rnd);
            if (check.search(text, p).isEmpty()) {
                return p;
            }
        }
    }

    private static void warmUp(StringMatcher[] matchers) {
        Random rnd = new Random(SEED + 1);
        String text = StringGenerator.random(20_000, rnd);
        String pattern = text.substring(10_000, 10_000 + PATTERN_LEN);
        for (int i = 0; i < 30; i++) {
            for (StringMatcher m : matchers) {
                sink = m.search(text, pattern).size();
            }
        }
    }

    private static double averageMillis(StringMatcher m, Scenario s) {
        long total = 0;
        for (int r = 0; r < RUNS; r++) {
            long start = System.nanoTime();
            List<Integer> res = m.search(s.text(), s.pattern());
            long end = System.nanoTime();
            total += end - start;
            sink = res.size();
        }
        return total / (double) RUNS / 1_000_000.0;
    }

    private static void writeCsv(Path path, List<String> rows) throws IOException {
        Files.createDirectories(path.getParent());
        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(path))) {
            pw.println("scenario,text_length,pattern_length,algorithm,avg_time_ms,comparisons,matches,spurious_hash_hits");
            for (String row : rows) {
                pw.println(row);
            }
        }
    }
}