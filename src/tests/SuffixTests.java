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
    }

}