package com.example.bookrating.util;

import com.example.bookrating.dto.UserDetailsDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class TokenExtractor {
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    public UserDetailsDTO getUserInfoFromToken(HttpServletRequest request) {

        String token = getJwtFromCookies(request);
        if(token==null) return null;

            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            UserDetailsDTO userDetailsDTO = new UserDetailsDTO();
           //userDTO.setProviderId(claims.getSubject());
            userDetailsDTO.setUsername(claims.get("username", String.class));
            userDetailsDTO.setEmail(claims.get("email", String.class));
            userDetailsDTO.setAvatar(claims.get("avatar", String.class));
            return userDetailsDTO;
    }

    private String getJwtFromCookies (HttpServletRequest request){
        if (request.getCookies() != null) {
            return Arrays.stream(request.getCookies())
                    .filter(cookie -> "jwt".equals(cookie.getName()))
                    .filter(cookie -> cookie.getValue() != null && !cookie.getValue().isBlank())
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }
}
