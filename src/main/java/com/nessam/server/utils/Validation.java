package com.nessam.server.utils;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {

    public static void checkMatchingPasswords(String password, String reapetedPass) throws SQLException {
        if (!password.equals(reapetedPass)) {
            throw new SQLException("Passwords do not match");
        }
    }

}
private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

private static final Pattern pattern = Pattern.compile(EMAIL_REGEX);

public static boolean isValidEmail(String email) {
    if (email == null)
        return false;
    Matcher matcher = pattern.matcher(email);
    return matcher.matches();
}

private static final String PASSWORD_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

public static boolean validatePassword(String password) {
    Matcher matcher = pattern.matcher(password);
    return matcher.matches();
}

public static boolean validatePassword(String password, String password2) {


    return password.equals(password2);
}