package tests;

public final class TestSupport {
    private static int passed = 0;
    private static int failed = 0;

    private TestSupport() {}

    public static void assertEquals(String name, Object expected, Object actual) {
        if (expected.equals(actual)) {
            passed++;
            System.out.println("PASS  " + name);
        } else {
            failed++;
            System.out.println("FAIL  " + name + "  expected=" + expected + "  actual=" + actual);
        }
    }

    public static void summary() {
        System.out.println();
        System.out.println(passed + " passed, " + failed + " failed");
    }
}