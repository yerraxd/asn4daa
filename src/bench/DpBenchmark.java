package bench;

import dp.EditDistance;
import dp.LCS;
import util.StringGenerator;

import java.util.Locale;
import java.util.Random;

public class DpBenchmark {
    private static final long SEED = 42L;
    private static final int[] SIZES = {100, 500, 1000};
    private static final int RUNS = 5;

    public static void main(String[] args) {
        Random rnd = new Random(SEED);
        System.out.printf("%6s %12s %12s%n", "n", "LCS (ms)", "Edit (ms)");

        for (int n : SIZES) {
            String a = StringGenerator.random(n, rnd);
            String b = StringGenerator.random(n, rnd);

            long lcsTotal = 0;
            long editTotal = 0;
            for (int r = 0; r < RUNS; r++) {
                long t0 = System.nanoTime();
                LCS.length(a, b);
                lcsTotal += System.nanoTime() - t0;

                t0 = System.nanoTime();
                EditDistance.distance(a, b);
                editTotal += System.nanoTime() - t0;
            }

            System.out.printf(Locale.ROOT, "%6d %12.4f %12.4f%n",
                    n, lcsTotal / (double) RUNS / 1e6, editTotal / (double) RUNS / 1e6);
        }
    }
}