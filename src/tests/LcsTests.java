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
        TestSupport.assertEquals("symmetry", LCS.length("AGGTAB", "GXTXAYB"), LCS.length("GXTXAYB", "AGGTAB"));
        String s = LCS.subsequence("ABCBDAB", "BDCABA");
        TestSupport.assertEquals("subsequence length", 4, s.length());
        TestSupport.assertEquals("subsequence in a", true, isSubsequence(s, "ABCBDAB"));
        TestSupport.assertEquals("subsequence in b", true, isSubsequence(s, "BDCABA"));
        TestSupport.assertEquals("subsequence identical", "ABCDEF", LCS.subsequence("ABCDEF", "ABCDEF"));
        TestSupport.assertEquals("subsequence different", "", LCS.subsequence("AAAA", "BBBB"));
        TestSupport.assertThrows("null first argument", () -> LCS.length(null, "A"));
    }
    private static boolean isSubsequence(String s, String t) {
        int i = 0;
        for (int j = 0; j < t.length() && i < s.length(); j++) {
            if (s.charAt(i) == t.charAt(j)) {
                i++;
            }
        }
        return i == s.length();
    }

}