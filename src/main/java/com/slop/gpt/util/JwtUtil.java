package com.slop.gpt.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    private final String secret;
    private final long expireSeconds;

    public JwtUtil(
            @Value("${slopgpt.jwt.secret:ReplaceWithAStrongSecretOf32CharsMinimum!}") String secret,
            @Value("${slopgpt.jwt.expire-seconds:3600}") long expireSeconds) {
        this.secret = secret;
        this.expireSeconds = expireSeconds;
    }

    public String generateToken(String userId) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + expireSeconds * 1000L);
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        Key key = Keys.hmacShaKeyFor(keyBytes);

        return Jwts.builder().setSubject(userId).setIssuedAt(now).setExpiration(exp)
                .signWith(key, SignatureAlgorithm.HS256).compact();
    }
}
