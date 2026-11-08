package matching;

import java.util.ArrayList;
import java.util.List;

public class RabinKarpMatcher implements StringMatcher {
    private final long base;
    private final long mod;

    public RabinKarpMatcher() {
        this(256, 1_000_000_007L);
    }

    public RabinKarpMatcher(long base, long mod) {
        if (mod < 2) {
            throw new IllegalArgumentException("mod must be at least 2");
        }
        this.base = base;
        this.mod = mod;
    }

    @Override
    public List<Integer> search(String text, String pattern) {
        List<Integer> result = new ArrayList<>();
        int n = text.length();
        int m = pattern.length();
        if (m == 0 || m > n) {
            return result;
        }
        long pow = 1;
        for (int i = 0; i < m - 1; i++) {
            pow = (pow * base) % mod;
        }

        long patHash = 0;
        long winHash = 0;
        for (int i = 0; i < m; i++) {
            patHash = (patHash * base + pattern.charAt(i)) % mod;
            winHash = (winHash * base + text.charAt(i)) % mod;
        }

        for (int i = 0; i + m <= n; i++) {
            if (patHash == winHash) {
                boolean match = true;
                for (int j = 0; j < m; j++) {
                    if (text.charAt(i + j) != pattern.charAt(j)) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    result.add(i);
                }
            }
            if (i + m < n) {
                winHash = (winHash - text.charAt(i) * pow % mod + mod) % mod;
                winHash = (winHash * base + text.charAt(i + m)) % mod;
            }
        }
        return result;
    }
}