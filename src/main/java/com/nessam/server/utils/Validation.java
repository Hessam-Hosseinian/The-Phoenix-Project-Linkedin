package com.nessam.server.utils;

import java.sql.SQLException;

public class Validation {

    public static void checkMatchingPasswords(String password, String reapetedPass) throws SQLException {
        if (!password.equals(reapetedPass)) {
            throw new SQLException("Passwords do not match");
        }
    }

}
