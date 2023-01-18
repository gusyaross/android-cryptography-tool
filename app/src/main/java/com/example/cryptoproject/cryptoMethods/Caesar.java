package com.example.cryptoproject.cryptoMethods;

public class Caesar {
    public static final String UPPER_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String LOWER_ALPHABET = "abcdefghijklmnopqrstuvwxyz";
    public static final String DIGITS = "0123456789";

    public static String decrypt(int key, String msg) {
        StringBuilder plaintext = new StringBuilder();

        for (int i = 0; i < msg.length(); i++) {
            char c = msg.charAt(i);

            String alphabet = null;

            if (UPPER_ALPHABET.indexOf(c) > -1) {
                alphabet = UPPER_ALPHABET;
            } else if (LOWER_ALPHABET.indexOf(c) > -1) {
                alphabet = LOWER_ALPHABET;
            } else if (DIGITS.indexOf(c) > -1) {
                alphabet = DIGITS;
            }

            if (alphabet != null) {
                int length = alphabet.length();

                c -= key;

                while (c > alphabet.charAt(length - 1)) {
                    c -= length;
                }
            }

            plaintext.append(c);
        }

        return plaintext.toString();
    }

    public static String encrypt(int key, String msg) {
        StringBuilder cipherText = new StringBuilder();

        for (int i = 0; i < msg.length(); i++) {
            char c = msg.charAt(i);

            String alphabet = null;

            if (UPPER_ALPHABET.indexOf(c) > -1) {
                alphabet = UPPER_ALPHABET;
            } else if (LOWER_ALPHABET.indexOf(c) > -1) {
                alphabet = LOWER_ALPHABET;
            } else if (DIGITS.indexOf(c) > -1) {
                alphabet = DIGITS;
            }

            if (alphabet != null) {
                int length = alphabet.length();

                c += key;

                while (c > alphabet.charAt(length - 1)) {
                    c -= length;
                }
            }

            cipherText.append(c);
        }

        return cipherText.toString();
    }
}
