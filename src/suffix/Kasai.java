package suffix;

public final class Kasai {
    private Kasai() {}

    public static int[] buildLcp(String s, int[] sa) {
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
                while (i + h < n && j + h < n && s.charAt(i + h) == s.charAt(j + h)) {
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
}