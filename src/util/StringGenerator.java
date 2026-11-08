package util;

import java.util.Random;

public final class StringGenerator {
    public static final String ALPHABET = "ACGT";

    private StringGenerator() {}

    public static String random(int length, Random rnd) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(ALPHABET.charAt(rnd.nextInt(ALPHABET.length())));
        }
        return sb.toString();
    }
}