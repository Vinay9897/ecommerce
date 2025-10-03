package com.ecommerce.apigateway.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    private volatile SecretKey signingKey;

    private SecretKey getSigningKey() {
        SecretKey localRef = signingKey;
        if (localRef == null) {
            synchronized (this) {
                localRef = signingKey;
                if (localRef == null) {
                    byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
                    signingKey = localRef = Keys.hmacShaKeyFor(keyBytes);
                }
            }
        }
        return localRef;
    }

    public Claims validateToken(String token) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("JWT token is blank");
        }
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public List<String> getRolesFromToken(String token) {
        Claims claims = validateToken(token);
        Object rolesClaim = claims.get("roles");
        if (rolesClaim == null) {
            return Collections.emptyList();
        }
        if (rolesClaim instanceof List<?>) {
            return ((List<?>) rolesClaim)
                    .stream()
                    .filter(Objects::nonNull)
                    .map(Object::toString)
                    .collect(Collectors.toList());
        }
        // Fallback in case roles were stored as a comma-separated string
        return List.of(rolesClaim.toString().split(","));
    }
}


