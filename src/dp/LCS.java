package dp;

public final class LCS {
    private LCS() {}

    static int[][] buildTable(String a, String b) {
        if (a == null || b == null) {
            throw new IllegalArgumentException("strings must not be null");
        }
        int n = a.length();
        int m = b.length();
        int[][] dp = new int[n + 1][m + 1];

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        return dp;
    }

    public static int length(String a, String b) {
        return buildTable(a, b)[a.length()][b.length()];
    }

    public static String subsequence(String a, String b) {
        int[][] dp = buildTable(a, b);
        StringBuilder sb = new StringBuilder();
        int i = a.length();
        int j = b.length();

        while (i > 0 && j > 0) {
            if (a.charAt(i - 1) == b.charAt(j - 1)) {
                sb.append(a.charAt(i - 1));
                i--;
                j--;
            } else if (dp[i - 1][j] >= dp[i][j - 1]) {
                i--;
            } else {
                j--;
            }
        }
        return sb.reverse().toString();
    }
}