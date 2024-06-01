package com.nessam.server.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTManager {
    private final Key secretKey = Keys.hmacShaKeyFor("WithGreatPowerComesGreatResponsibilityRememberThat".getBytes());
    private final SignatureAlgorithm algorithm = SignatureAlgorithm.HS256;

    public String createToken(Map<String, Object> payload, int expirationMinutes) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date exp = new Date(nowMillis + (long) expirationMinutes * 60 * 1000);

        return Jwts.builder().setClaims(payload).setIssuedAt(now).setExpiration(exp).signWith(secretKey, algorithm).compact();
    }

    public Map<String, Object> decodeToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
            return new HashMap<>(claims);
        } catch (JwtException e) {
            System.out.println("Invalid or expired token: " + e.getMessage());
            return null;
        }
    }
}
//this is a test comment