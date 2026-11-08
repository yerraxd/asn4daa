package tests;

import matching.NaiveMatcher;
import matching.StringMatcher;
import matching.KMPMatcher;
import java.util.Random;
import matching.RabinKarpMatcher;

import java.util.List;

public class MatcherTests {

    static StringMatcher[] matchers() {
        return new StringMatcher[]{new NaiveMatcher(), new KMPMatcher(), new RabinKarpMatcher()};
    }

    private static void check(String name, String text, String pattern, List<Integer> expected) {
        for (StringMatcher m : matchers()) {
            TestSupport.assertEquals(m.getClass().getSimpleName() + ": " + name,
                    expected, m.search(text, pattern));
        }
    }

    public static void run() {
        System.out.println("== String matching ==");
        check("pattern at the beginning", "ABCDEF", "ABC", List.of(0));
        check("pattern in the middle", "XXABCXX", "ABC", List.of(2));
        check("pattern at the end", "XXXABC", "ABC", List.of(3));
        check("multiple occurrences (overlapping)", "ABABABA", "ABA", List.of(0, 2, 4));
        check("all positions overlap", "AAAAA", "AA", List.of(0, 1, 2, 3));
        check("pattern absent", "ABCDEF", "XYZ", List.of());
        check("pattern longer than text", "AB", "ABCD", List.of());
        check("pattern equals text", "ABC", "ABC", List.of(0));
        check("single characters", "A", "A", List.of(0));
        randomCrossCheck();
    }
    private static String randomString(Random rnd, int length, String alphabet) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(alphabet.charAt(rnd.nextInt(alphabet.length())));
        }
        return sb.toString();
    }

    private static void randomCrossCheck() {
        Random rnd = new Random(7);
        boolean allEqual = true;
        for (int t = 0; t < 500; t++) {
            String text = randomString(rnd, 1 + rnd.nextInt(60), "AB");
            String pattern = randomString(rnd, 1 + rnd.nextInt(5), "AB");
            List<Integer> expected = new NaiveMatcher().search(text, pattern);
            for (StringMatcher m : matchers()) {
                if (!expected.equals(m.search(text, pattern))) {
                    allEqual = false;
                    System.out.println("MISMATCH " + m.getClass().getSimpleName()
                            + " text=" + text + " pattern=" + pattern);
                }
            }
        }
        TestSupport.assertEquals("random cross-check (500 cases, alphabet AB)", true, allEqual);
    }
}