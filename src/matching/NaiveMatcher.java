package matching;

import java.util.ArrayList;
import java.util.List;

public class NaiveMatcher implements StringMatcher {

    @Override
    public List<Integer> search(String text, String pattern) {
        List<Integer> result = new ArrayList<>();
        int n = text.length();
        int m = pattern.length();

        for (int i = 0; i + m <= n; i++) {
            int j = 0;
            while (j < m && text.charAt(i + j) == pattern.charAt(j)) {
                j++;
            }
            if (j == m) {
                result.add(i);
            }
        }
        return result;
    }
}