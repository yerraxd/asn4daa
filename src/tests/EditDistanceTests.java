package tests;

import dp.EditDistance;

public class EditDistanceTests {
    public static void run() {
        System.out.println("== Edit Distance ==");
        TestSupport.assertEquals("identical", 0, EditDistance.distance("hello", "hello"));
        TestSupport.assertEquals("first empty", 3, EditDistance.distance("", "abc"));
        TestSupport.assertEquals("second empty", 3, EditDistance.distance("abc", ""));
        TestSupport.assertEquals("both empty", 0, EditDistance.distance("", ""));
        TestSupport.assertEquals("substitution", 1, EditDistance.distance("cat", "cut"));
        TestSupport.assertEquals("insertion", 1, EditDistance.distance("cat", "cart"));
        TestSupport.assertEquals("deletion", 1, EditDistance.distance("cart", "cat"));
        TestSupport.assertEquals("kitten / sitting", 3, EditDistance.distance("kitten", "sitting"));
        TestSupport.assertEquals("flaw / lawn", 2, EditDistance.distance("flaw", "lawn"));
        TestSupport.assertEquals("sunday / saturday", 3, EditDistance.distance("sunday", "saturday"));
        TestSupport.assertEquals("intention / execution", 5, EditDistance.distance("intention", "execution"));
        TestSupport.assertEquals("symmetry",
                EditDistance.distance("kitten", "sitting"), EditDistance.distance("sitting", "kitten"));
        TestSupport.assertThrows("null second argument", () -> EditDistance.distance("A", null));
    }
}