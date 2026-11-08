package suffix;

import java.util.Arrays;

public final class SuffixArray {
    private SuffixArray() {}
    public static int[] build(String s) {
        int n = s.length();
        Integer[] idx = new Integer[n];
        for (int i = 0; i < n; i++) {
            idx[i] = i;
        }
        Arrays.sort(idx, (x, y) -> compareSuffixes(s, x, y));

        int[] sa = new int[n];
        for (int i = 0; i < n; i++) {
            sa[i] = idx[i];
        }
        return sa;
    }

    static int compareSuffixes(String s, int a, int b) {
        int n = s.length();
        while (a < n && b < n) {
            char ca = s.charAt(a);
            char cb = s.charAt(b);
            if (ca != cb) {
                return Character.compare(ca, cb);
            }
            a++;
            b++;
        }
        if (a == n && b == n) {
            return 0;
        }
        return a == n ? -1 : 1;
    }
}