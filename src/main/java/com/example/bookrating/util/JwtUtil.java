package com.example.bookrating.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    @Autowired
    private Environment env;

    public String parseToken(String token) {

        if (token == null || token.trim().isEmpty()) {
            return null; // 유효하지 않은 토큰으로 간주하고 null 반환
        }

        String secretKey = env.getProperty("jwt.secret");

        if (secretKey == null) {
            throw new IllegalStateException("JWT Secret Key is not configured in environment properties.");
        }

        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}