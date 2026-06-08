
package com.lms.authservice.util;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {

    private static final String SECRET = "LeaveManagementSystemSecretKeyForJWTAuthentication2026";

    private Key getSigningKey() {

        return new SecretKeySpec(SECRET.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS256.getJcaName());
    }

    public String generateToken(Long userId, String username, String role) {

        return Jwts.builder().subject(username).claim("userId", userId).claim("role", role).issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + 86400000)).signWith(getSigningKey()).compact();
    }

    public String extractUsername(String token) {

        return Jwts.parser().verifyWith((javax.crypto.SecretKey) getSigningKey()).build().parseSignedClaims(token)
            .getPayload().getSubject();
    }

    public boolean validateToken(String token) {

        try {

            Jwts.parser().verifyWith((javax.crypto.SecretKey) getSigningKey()).build().parseSignedClaims(token);

            return true;

        } catch (Exception ex) {

            return false;
        }
    }

}