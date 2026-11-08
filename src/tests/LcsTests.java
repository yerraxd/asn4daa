package tests;

import dp.LCS;

public class LcsTests {
    public static void run() {
        System.out.println("== LCS ==");
        TestSupport.assertEquals("identical strings", 6, LCS.length("ABCDEF", "ABCDEF"));
        TestSupport.assertEquals("completely different", 0, LCS.length("AAAA", "BBBB"));
        TestSupport.assertEquals("first empty", 0, LCS.length("", "ABC"));
        TestSupport.assertEquals("second empty", 0, LCS.length("ABC", ""));
        TestSupport.assertEquals("both empty", 0, LCS.length("", ""));
        TestSupport.assertEquals("ABCBDAB / BDCABA", 4, LCS.length("ABCBDAB", "BDCABA"));
        TestSupport.assertEquals("AGGTAB / GXTXAYB", 4, LCS.length("AGGTAB", "GXTXAYB"));
        TestSupport.assertEquals("symmetry",
                LCS.length("AGGTAB", "GXTXAYB"), LCS.length("GXTXAYB", "AGGTAB"));
    }
}