package tests;

import suffix.Kasai;
import suffix.SuffixArray;

import java.util.Arrays;

public class SuffixTests {
    public static void run() {
        System.out.println("== Suffix array and Kasai ==");

        String s = "banana";
        int[] sa = SuffixArray.build(s);
        TestSupport.assertEquals("banana: suffix array", "[5, 3, 1, 0, 4, 2]", Arrays.toString(sa));

        int[] manualSa = {5, 3, 1, 0, 4, 2};
        TestSupport.assertEquals("banana: LCP from manual SA", "[0, 1, 3, 0, 0, 2]",
                Arrays.toString(Kasai.buildLcp(s, manualSa)));
    }
}