package bench;

import matching.KMPMatcher;
import matching.NaiveMatcher;
import matching.RabinKarpMatcher;
import matching.StringMatcher;
import util.StringGenerator;

import java.util.List;
import java.util.Locale;
import java.util.Random;

public class MatchingBenchmark {
    private static final long SEED = 42L;
    private static final int[] TEXT_SIZES = {10_000, 50_000, 100_000};
    private static final int PATTERN_LEN = 20;
    private static final int RUNS = 5;

    public static void main(String[] args) {
        StringMatcher[] matchers = {new NaiveMatcher(), new KMPMatcher(), new RabinKarpMatcher()};
        Random rnd = new Random(SEED);

        for (int n : TEXT_SIZES) {
            String text = StringGenerator.random(n, rnd);
            String present = text.substring(n / 2, n / 2 + PATTERN_LEN);
            String absent = makeAbsentPattern(text, rnd);

            for (StringMatcher m : matchers) {
                for (String pattern : new String[]{present, absent}) {
                    long total = 0;
                    for (int r = 0; r < RUNS; r++) {
                        long start = System.nanoTime();
                        List<Integer> res = m.search(text, pattern);
                        total += System.nanoTime() - start;
                    }
                    System.out.printf(Locale.ROOT, "%8d %-18s %8.4f ms%n",
                            n, m.getClass().getSimpleName(), total / (double) RUNS / 1e6);
                }
            }
        }
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
}