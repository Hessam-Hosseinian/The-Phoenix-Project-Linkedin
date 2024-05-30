package com.nessam.server.utils;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class JWTManagerTest {

    private final JWTManager jwtManager = new JWTManager();

    @Test
    public void testCreateToken() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("username", "testUser");
        payload.put("role", "admin");

        String token = jwtManager.createToken(payload, 10);
        assertNotNull(token);
        assertTrue(token.length() > 0);

        // Verify token structure
        String[] parts = token.split("\\.");
        assertEquals(3, parts.length, "JWT token should have three parts separated by dots");
    }

    @Test
    public void testDecodeToken() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("username", "testUser");
        payload.put("role", "admin");

        String token = jwtManager.createToken(payload, 10);
        Map<String, Object> decodedPayload = jwtManager.decodeToken(token);

        assertNotNull(decodedPayload);
        assertEquals("testUser", decodedPayload.get("username"));
        assertEquals("admin", decodedPayload.get("role"));
    }

    @Test
    public void testDecodeExpiredToken() throws InterruptedException {
        Map<String, Object> payload = new HashMap<>();
        payload.put("username", "testUser");

        // Create a token that expires immediately
        String token = jwtManager.createToken(payload, 0);
        Thread.sleep(1000); // Wait for 1 second to ensure the token has expired

        Map<String, Object> decodedPayload = jwtManager.decodeToken(token);
        assertNull(decodedPayload);
    }

    @Test
    public void testDecodeInvalidToken() {
        // An invalid token
        String invalidToken = "invalid.token.here";

        Map<String, Object> decodedPayload = jwtManager.decodeToken(invalidToken);
        assertNull(decodedPayload);
    }
}
