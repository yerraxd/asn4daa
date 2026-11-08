package tests;

import matching.NaiveMatcher;
import matching.StringMatcher;

import java.util.List;

public class MatcherTests {

    static StringMatcher[] matchers() {
        return new StringMatcher[]{new NaiveMatcher()};
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
    }
}