package tests;

public class AllTests {
    public static void main(String[] args) {
        LcsTests.run();
        EditDistanceTests.run();
        KmpLpsTests.run();
        MatcherTests.run();
        CounterTests.run();
        TestSupport.summary();
    }
}