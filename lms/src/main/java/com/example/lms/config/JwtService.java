package com.example.lms.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {
    @Value( "${jwt.secret-key}")
    private  String SECRET_KEY;

    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24; //24h

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    /* ---------- CREATE TOKEN ---------- */
    public String generateToken(String email, String role) {

        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /* ---------- VALIDATE TOKEN ---------- */
    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /* ---------- EXTRACT EMAIL ---------- */
    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    /* ---------- EXTRACT ROLE ---------- */
    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    /* ---------- INTERNAL ---------- */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
