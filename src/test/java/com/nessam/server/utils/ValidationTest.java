package com.nessam.server.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ValidationTest {

    @Test
    public void testIsValidEmail() {
        // Valid email addresses
        assertTrue(Validation.isValidEmail("test@example.com"));
        assertTrue(Validation.isValidEmail("user.name+tag+sorting@example.com"));
        assertTrue(Validation.isValidEmail("user_name@example.co.uk"));
        assertTrue(Validation.isValidEmail("user-name@domain.org"));

        // Invalid email addresses
        assertFalse(Validation.isValidEmail("plainaddress"));
        assertFalse(Validation.isValidEmail("@missingusername.com"));
        assertFalse(Validation.isValidEmail("username@.com"));
        assertFalse(Validation.isValidEmail("username@.com.com"));
        assertFalse(Validation.isValidEmail("username@domain..com"));
    }

    @Test
    public void testValidatePassword() {
        // Valid passwords
        assertTrue(Validation.validatePassword("Password1"));
        assertTrue(Validation.validatePassword("1234abcd"));

        // Invalid passwords
        assertFalse(Validation.validatePassword("short1"));
        assertFalse(Validation.validatePassword("noNumbers"));
        assertFalse(Validation.validatePassword("12345678"));
        assertFalse(Validation.validatePassword("abcdefgh"));
    }

    @Test
    public void testMatchingPasswords() {
        // Matching passwords
        assertTrue(Validation.matchingPasswords("Password123", "Password123"));
        assertTrue(Validation.matchingPasswords("1234abcd", "1234abcd"));

        // Non-matching passwords
        assertFalse(Validation.matchingPasswords("Password123", "password123"));
        assertFalse(Validation.matchingPasswords("1234abcd", "abcd1234"));
    }
}
