package tests;

import suffix.Kasai;
import suffix.SuffixArray;
import util.StringGenerator;

import java.util.Arrays;
import java.util.Random;

public class SuffixTests {
    public static void run() {
        System.out.println("== Suffix array and Kasai ==");

        String s = "banana";
        int[] sa = SuffixArray.build(s);
        TestSupport.assertEquals("banana: suffix array", "[5, 3, 1, 0, 4, 2]", Arrays.toString(sa));

        int[] manualSa = {5, 3, 1, 0, 4, 2};
        TestSupport.assertEquals("banana: LCP from manual SA", "[0, 1, 3, 0, 0, 2]",
                Arrays.toString(Kasai.buildLcp(s, manualSa)));
        String abra = "abracadabra";
        int[] abraSa = {10, 7, 0, 3, 5, 8, 1, 4, 6, 9, 2};
        TestSupport.assertEquals("abracadabra: suffix array", Arrays.toString(abraSa),
                Arrays.toString(SuffixArray.build(abra)));
        TestSupport.assertEquals("abracadabra: LCP", "[0, 1, 4, 1, 1, 0, 3, 0, 0, 0, 2]",
                Arrays.toString(Kasai.buildLcp(abra, abraSa)));

        String same = "aaaa";
        int[] sameSa = SuffixArray.build(same);
        TestSupport.assertEquals("aaaa: suffix array", "[3, 2, 1, 0]", Arrays.toString(sameSa));
        TestSupport.assertEquals("aaaa: LCP", "[0, 1, 2, 3]", Arrays.toString(Kasai.buildLcp(same, sameSa)));

        TestSupport.assertEquals("single char: LCP", "[0]",
                Arrays.toString(Kasai.buildLcp("a", SuffixArray.build("a"))));
        TestSupport.assertEquals("empty string: LCP", "[]",
                Arrays.toString(Kasai.buildLcp("", SuffixArray.build(""))));
        TestSupport.assertThrows("SA length mismatch", () -> Kasai.buildLcp("abc", new int[]{0, 1}));
        TestSupport.assertThrows("SA index out of range", () -> Kasai.buildLcp("abc", new int[]{0, 1, 5}));
        TestSupport.assertThrows("null string", () -> Kasai.buildLcp(null, new int[0]));
        randomCrossCheck();
    }
    private static int[] bruteForceLcp(String s, int[] sa) {
        int n = s.length();
        int[] lcp = new int[n];
        for (int i = 1; i < n; i++) {
            int a = sa[i - 1];
            int b = sa[i];
            int k = 0;
            while (a + k < n && b + k < n && s.charAt(a + k) == s.charAt(b + k)) {
                k++;
            }
            lcp[i] = k;
        }
        return lcp;
    }

    private static void randomCrossCheck() {
        Random rnd = new Random(21);
        boolean allEqual = true;
        boolean withinBound = true;
        for (int t = 0; t < 300; t++) {
            int len = 1 + rnd.nextInt(80);
            String alphabet = (t % 2 == 0) ? "AB" : StringGenerator.ALPHABET;
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < len; i++) {
                sb.append(alphabet.charAt(rnd.nextInt(alphabet.length())));
            }
            String s = sb.toString();
            int[] sa = SuffixArray.build(s);

            Kasai kasai = new Kasai();
            int[] fast = kasai.build(s, sa);
            if (!Arrays.equals(fast, bruteForceLcp(s, sa))) {
                allEqual = false;
                System.out.println("MISMATCH for " + s);
            }
            if (kasai.getComparisons() > 3L * len) {
                withinBound = false;
            }
        }
        TestSupport.assertEquals("Kasai equals brute force on 300 random strings", true, allEqual);
        TestSupport.assertEquals("Kasai comparisons stay within 3n", true, withinBound);
    }
}