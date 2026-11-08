package bench;

import suffix.Kasai;
import suffix.SuffixArray;
import util.StringGenerator;

import java.util.Locale;
import java.util.Random;

public class KasaiBenchmark {
    private static final long SEED = 42L;
    private static final int[] SIZES = {1000, 5000, 10000};
    private static final int RUNS = 5;

    public static void main(String[] args) {
        Random rnd = new Random(SEED);
        System.out.printf("%8s %12s %14s%n", "n", "time (ms)", "comparisons");

        for (int n : SIZES) {
            String s = StringGenerator.random(n, rnd);
            int[] sa = SuffixArray.build(s);

            long total = 0;
            Kasai kasai = new Kasai();
            for (int r = 0; r < RUNS; r++) {
                long start = System.nanoTime();
                kasai.build(s, sa);
                total += System.nanoTime() - start;
            }
            System.out.printf(Locale.ROOT, "%8d %12.4f %14d%n",
                    n, total / (double) RUNS / 1e6, kasai.getComparisons());
        }
    }
}