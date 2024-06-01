package com.nessam.server.utils;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {

    private static final String  EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private static final String PASSWORD_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
    private static final Pattern emailPattern = Pattern.compile(EMAIL_REGEX);
    private static final Pattern passPattern = Pattern.compile(PASSWORD_PATTERN);

    public static boolean isValidEmail(String email) {
        if (email == null)
            return false;
        Matcher matcher = emailPattern.matcher(email);
        return matcher.matches();
    }

    public static boolean validatePassword(String password) {
        Matcher matcher = passPattern.matcher(password);
        return matcher.matches();
    }

    public static boolean matchingPasswords(String password, String password2) {
        return password.equals(password2);
    }

}


//this is a test comment