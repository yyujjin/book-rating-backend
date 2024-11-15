package com.example.bookrating.util;

import com.example.bookrating.dto.UserDTO;
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

    public UserDTO getUserInfoFromToken(HttpServletRequest request) {

        String token = getJwtFromCookies(request);
        if(token==null) return null;

            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            UserDTO userDTO = new UserDTO();
            userDTO.setProviderId(claims.getSubject());
            userDTO.setEmail(claims.get("email", String.class));
            userDTO.setAvatar(claims.get("avatar", String.class));
            System.out.println("사용자 고유 아이디 :" + claims.getSubject() + "\n 사용자 이메일 : " + userDTO.getEmail() + "\n 사용자 이미지 : " + userDTO.getAvatar());

            return userDTO;
    }

    private String getJwtFromCookies (HttpServletRequest request){
        if (request.getCookies() != null) {
            return Arrays.stream(request.getCookies())
                    .filter(cookie -> "jwt".equals(cookie.getName()))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }
}
