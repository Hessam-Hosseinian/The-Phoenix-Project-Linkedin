package com.nessam.server.utils;

public class JwtAuth {
    private static final String secret = "I_LOVE_PIZZA";

    public static String jws(String userID) {
        char[] charArray = new char[userID.length() + 1];
        charArray[0] = '@';
        for (int i = 0; i < userID.length(); i++) {
            charArray[i + 1] = (char) (48 + ((charArray[i] + (int) userID.charAt(i) ^ (int) secret.charAt(i % secret.length())) % 50));
        }
        return new String(charArray);
    }
}
