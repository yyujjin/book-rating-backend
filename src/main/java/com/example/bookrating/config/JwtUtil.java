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

    public String parseToken(String token){

        String secretKey = env.getProperty("jwt.secret");
        if (secretKey==null) return "";
        String username = "";

        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            username = claims.get("username", String.class);


        } catch (Exception e) {
            e.printStackTrace();
        }

        return username;
    }
}
