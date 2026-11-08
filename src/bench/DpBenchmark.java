package bench;
import dp.EditDistance;
import dp.LCS;
import util.StringGenerator;
import java.util.Locale;
import java.util.Random;
import java.util.function.IntSupplier;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DpBenchmark {
    private static final long SEED = 42L;
    private static final int[] SIZES = {100, 500, 1000};
    private static final int RUNS = 5;
    private static volatile int sink;

    public static void main(String[] args) throws IOException {
        Random rnd = new Random(SEED);
        String[] as = new String[SIZES.length];
        String[] bs = new String[SIZES.length];
        for (int i = 0; i < SIZES.length; i++) {
            as[i] = StringGenerator.random(SIZES[i], rnd);
            bs[i] = StringGenerator.random(SIZES[i], rnd);
        }

        warmUp();

        double[] lcsMs = new double[SIZES.length];
        double[] editMs = new double[SIZES.length];
        int[] lcsValue = new int[SIZES.length];
        int[] editValue = new int[SIZES.length];

        for (int i = 0; i < SIZES.length; i++) {
            final String a = as[i];
            final String b = bs[i];
            lcsMs[i] = averageMillis(() -> LCS.length(a, b));
            editMs[i] = averageMillis(() -> EditDistance.distance(a, b));
            lcsValue[i] = LCS.length(a, b);
            editValue[i] = EditDistance.distance(a, b);
        }

        printTable(lcsMs, editMs, lcsValue, editValue);
        writeCsv(Paths.get("results", "dp_results.csv"), lcsMs, editMs, lcsValue, editValue);
    }

    private static void warmUp() {
        Random warm = new Random(SEED + 1);
        String wa = StringGenerator.random(500, warm);
        String wb = StringGenerator.random(500, warm);
        for (int i = 0; i < 20; i++) {
            sink = LCS.length(wa, wb);
            sink = EditDistance.distance(wa, wb);
        }
    }

    private static double averageMillis(IntSupplier task) {
        long total = 0;
        for (int r = 0; r < RUNS; r++) {
            long start = System.nanoTime();
            int result = task.getAsInt();
            long end = System.nanoTime();
            total += end - start;
            sink = result;
        }
        return total / (double) RUNS / 1_000_000.0;
    }

    private static void printTable(double[] lcsMs, double[] editMs, int[] lcsValue, int[] editValue) {
        System.out.printf("%6s %12s %12s %12s %10s %10s%n",
                "n", "LCS (ms)", "Edit (ms)", "cells", "LCS len", "Edit dist");
        for (int i = 0; i < SIZES.length; i++) {
            long cells = (long) (SIZES[i] + 1) * (SIZES[i] + 1);
            System.out.printf(Locale.ROOT, "%6d %12.4f %12.4f %12d %10d %10d%n",
                    SIZES[i], lcsMs[i], editMs[i], cells, lcsValue[i], editValue[i]);
        }
    }
    private static void writeCsv(Path path, double[] lcsMs, double[] editMs,
                                 int[] lcsValue, int[] editValue) throws IOException {
        Files.createDirectories(path.getParent());
        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(path))) {
            pw.println("n,lcs_ms,edit_distance_ms,table_cells,lcs_length,edit_distance");
            for (int i = 0; i < SIZES.length; i++) {
                long cells = (long) (SIZES[i] + 1) * (SIZES[i] + 1);
                pw.printf(Locale.ROOT, "%d,%.4f,%.4f,%d,%d,%d%n",
                        SIZES[i], lcsMs[i], editMs[i], cells, lcsValue[i], editValue[i]);
            }
        }
    }
}