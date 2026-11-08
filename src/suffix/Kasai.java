package suffix;

public final class Kasai {
    private long comparisons;

    public static int[] buildLcp(String s, int[] sa) {
        return new Kasai().build(s, sa);
    }
    public int[] build(String s, int[] sa) {
        comparisons = 0;
        if (s == null || sa == null) {
            throw new IllegalArgumentException("arguments must not be null");
        }
        int n = s.length();
        if (sa.length != n) {
            throw new IllegalArgumentException("suffix array length must equal string length");
        }
        int[] rank = new int[n];
        for (int i = 0; i < n; i++) {
            if (sa[i] < 0 || sa[i] >= n) {
                throw new IllegalArgumentException("suffix array contains index out of range");
            }
            rank[sa[i]] = i;
        }

        int[] lcp = new int[n];
        int h = 0;
        for (int i = 0; i < n; i++) {
            if (rank[i] > 0) {
                int j = sa[rank[i] - 1];
                while (i + h < n && j + h < n) {
                    comparisons++;
                    if (s.charAt(i + h) != s.charAt(j + h)) {
                        break;
                    }
                    h++;
                }
                lcp[rank[i]] = h;
                if (h > 0) {
                    h--;
                }
            } else {
                h = 0;
            }
        }
        return lcp;
    }

    public long getComparisons() {
        return comparisons;
    }
}