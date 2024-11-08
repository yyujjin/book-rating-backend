package com.example.bookrating.config;

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
            return claims.get("username", String.class);
        } catch (Exception e) {
            e.printStackTrace();  // 필요에 따라 로그 또는 사용자 정의 예외로 변경 가능
            return null;  // 토큰이 유효하지 않으면 null 반환
        }
    }
}
