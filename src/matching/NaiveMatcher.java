package matching;

import java.util.ArrayList;
import java.util.List;

public class NaiveMatcher implements StringMatcher {
    private long comparisons;

    @Override
    public List<Integer> search(String text, String pattern) {
        comparisons = 0;
        List<Integer> result = new ArrayList<>();
        if (pattern.isEmpty()) {
            return result;
        }
        int n = text.length();
        int m = pattern.length();

        for (int i = 0; i + m <= n; i++) {
            int j = 0;
            while (j < m) {
                comparisons++;
                if (text.charAt(i + j) != pattern.charAt(j)) {
                    break;
                }
                j++;
            }
            if (j == m) {
                result.add(i);
            }
        }
        return result;
    }

    @Override
    public long getComparisons() {
        return comparisons;
    }
}