package bench;

import suffix.Kasai;
import suffix.SuffixArray;
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

public class KasaiBenchmark {
    private static final long SEED = 42L;
    private static final int[] SIZES = {1000, 5000, 10000};
    private static final int RUNS = 5;

    private static volatile int sink;

    private record Scenario(String name, String text, int[] sa) {}

    public static void main(String[] args) throws IOException {
        Random rnd = new Random(SEED);
        List<Scenario> scenarios = new ArrayList<>();
        for (int n : SIZES) {
            String random = StringGenerator.random(n, rnd);
            scenarios.add(new Scenario("random", random, SuffixArray.build(random)));

            String repetitive = "A".repeat(n);
            scenarios.add(new Scenario("repetitive", repetitive, SuffixArray.build(repetitive)));
        }

        warmUp();

        List<String> csvRows = new ArrayList<>();
        System.out.printf("%-11s %8s %12s %14s %10s%n",
                "scenario", "n", "time (ms)", "comparisons", "cmp / n");

        for (Scenario s : scenarios) {
            Kasai kasai = new Kasai();
            long total = 0;
            for (int r = 0; r < RUNS; r++) {
                long start = System.nanoTime();
                int[] lcp = kasai.build(s.text(), s.sa());
                long end = System.nanoTime();
                total += end - start;
                sink = lcp[lcp.length - 1];
            }
            double ms = total / (double) RUNS / 1_000_000.0;
            long comparisons = kasai.getComparisons();
            int n = s.text().length();

            System.out.printf(Locale.ROOT, "%-11s %8d %12.4f %14d %10.3f%n",
                    s.name(), n, ms, comparisons, comparisons / (double) n);
            csvRows.add(String.format(Locale.ROOT, "%s,%d,%.4f,%d",
                    s.name(), n, ms, comparisons));
        }

        writeCsv(Paths.get("results", "kasai_results.csv"), csvRows);
    }

    private static void warmUp() {
        Random rnd = new Random(SEED + 1);
        String s = StringGenerator.random(5000, rnd);
        int[] sa = SuffixArray.build(s);
        Kasai kasai = new Kasai();
        for (int i = 0; i < 50; i++) {
            sink = kasai.build(s, sa)[s.length() - 1];
        }
    }

    private static void writeCsv(Path path, List<String> rows) throws IOException {
        Files.createDirectories(path.getParent());
        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(path))) {
            pw.println("scenario,n,avg_time_ms,comparisons");
            for (String row : rows) {
                pw.println(row);
            }
        }
    }
}