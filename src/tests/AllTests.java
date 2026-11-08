package tests;

public class AllTests {
    public static void main(String[] args) {
        LcsTests.run();
        EditDistanceTests.run();
        KmpLpsTests.run();
        MatcherTests.run();
        CounterTests.run();
        SuffixTests.run();
        TestSupport.summary();
    }
}