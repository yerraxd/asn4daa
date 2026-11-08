package matching;

import java.util.ArrayList;
import java.util.List;

public class KMPMatcher implements StringMatcher {
    private long comparisons;
    public static int[] buildLps(String p) {
        int m = p.length();
        int[] lps = new int[m];
        int len = 0;
        int i = 1;

        while (i < m) {
            if (p.charAt(i) == p.charAt(len)) {
                len++;
                lps[i] = len;
                i++;
            } else if (len > 0) {
                len = lps[len - 1];
            } else {
                lps[i] = 0;
                i++;
            }
        }
        return lps;
    }

    @Override
    public List<Integer> search(String text, String pattern) {
        comparisons = 0;
        List<Integer> result = new ArrayList<>();
        if (pattern.isEmpty()) {
            return result;
        }
        int n = text.length();
        int m = pattern.length();
        int[] lps = buildLps(pattern);

        int i = 0;
        int j = 0;
        while (i < n) {
            comparisons++;
            if (text.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;
                if (j == m) {
                    result.add(i - m);
                    j = lps[j - 1];
                }
            } else if (j > 0) {
                j = lps[j - 1];
            } else {
                i++;
            }
        }
        return result;
    }

    @Override
    public long getComparisons() {
        return comparisons;
    }
}