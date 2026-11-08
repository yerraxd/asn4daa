package tests;

import matching.KMPMatcher;

import java.util.Arrays;

public class KmpLpsTests {
    public static void run() {
        System.out.println("== KMP LPS table ==");
        TestSupport.assertEquals("ABABCABAB", "[0, 0, 1, 2, 0, 1, 2, 3, 4]",
                Arrays.toString(KMPMatcher.buildLps("ABABCABAB")));
        TestSupport.assertEquals("AAAA", "[0, 1, 2, 3]",
                Arrays.toString(KMPMatcher.buildLps("AAAA")));
        TestSupport.assertEquals("ABCD", "[0, 0, 0, 0]",
                Arrays.toString(KMPMatcher.buildLps("ABCD")));
        TestSupport.assertEquals("AABAAAB", "[0, 1, 0, 1, 2, 2, 3]",
                Arrays.toString(KMPMatcher.buildLps("AABAAAB")));
    }
}