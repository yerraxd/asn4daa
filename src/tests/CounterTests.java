package tests;

import matching.KMPMatcher;
import matching.NaiveMatcher;
import matching.RabinKarpMatcher;
import util.StringGenerator;

import java.util.Random;

public class CounterTests {
    public static void run() {
        System.out.println("== Comparison counters ==");

        NaiveMatcher naive = new NaiveMatcher();
        naive.search("AAAAAAAAAA", "AAB");
        TestSupport.assertEquals("Naive: 8 windows x 3 comparisons", 24L, naive.getComparisons());
        naive.search("BBBBBBBBBB", "AAB");
        TestSupport.assertEquals("Naive: 8 windows x 1 comparison", 8L, naive.getComparisons());

        String worstText = "A".repeat(1000);
        String worstPattern = "AAAAB";
        naive.search(worstText, worstPattern);
        TestSupport.assertEquals("Naive worst case: 996 windows x 5", 4980L, naive.getComparisons());

        KMPMatcher kmp = new KMPMatcher();
        kmp.search(worstText, worstPattern);
        TestSupport.assertEquals("KMP worst-case text stays within 2n",
                true, kmp.getComparisons() <= 2L * worstText.length());
        TestSupport.assertEquals("KMP does fewer comparisons than Naive on worst case",
                true, kmp.getComparisons() < naive.getComparisons());

        String random = StringGenerator.random(1000, new Random(11));
        kmp.search(random, "AAAAC");
        TestSupport.assertEquals("KMP random text stays within 2n",
                true, kmp.getComparisons() <= 2L * random.length());

        RabinKarpMatcher rk = new RabinKarpMatcher();
        rk.search("ABCABC", "ABC");
        TestSupport.assertEquals("RK: comparisons only on hash hits (2 x 3)", 6L, rk.getComparisons());
        TestSupport.assertEquals("RK: no spurious hits with large modulus", 0L, rk.getSpuriousHits());

        RabinKarpMatcher weak = new RabinKarpMatcher(3, 7);
        weak.search(StringGenerator.random(1000, new Random(5)), "ACGTAC");
        TestSupport.assertEquals("RK with mod 7 has spurious hits", true, weak.getSpuriousHits() > 0);
    }
}