package tests;

public class AllTests {
    public static void main(String[] args) {
        LcsTests.run();
        TestSupport.summary();
        EditDistanceTests.run();
        MatcherTests.run();
        CounterTests.run();
        KmpLpsTests.run();
    }
}