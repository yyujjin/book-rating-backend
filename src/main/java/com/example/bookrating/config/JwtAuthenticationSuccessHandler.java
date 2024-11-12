package com.example.bookrating.config;

import com.example.bookrating.dto.CustomOAuth2User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class JwtAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Value("${frontend.url}")
    private String frontendUrl;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException, java.io.IOException {

        authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();

// 사용자 정보 접근
        String providerId = customOAuth2User.getProviderId();
        String email = customOAuth2User.getUserEmail();
        String avatar = customOAuth2User.getAvatar();

        // JWT 토큰 생성
        String token = Jwts.builder()
                .setSubject(providerId) // 구글 고유 아이디
                //.claim("authorities", authentication.getAuthorities()) // 권한 정보
               // .claim("username", userSessionService.getUserName())
                .claim("email", email)
                .claim("avatar", avatar)
                .setIssuedAt(new Date()) //JWT 토큰이 발급된 시간
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1일 유효
                .signWith(SignatureAlgorithm.HS256, jwtSecret.getBytes()) // 서명 알고리즘
                .compact();

        // JWT를 HttpOnly 쿠키에 저장
        Cookie jwtCookie = new Cookie("jwt", token);
        jwtCookie.setHttpOnly(true);  // 자바스크립트 접근 금지
        jwtCookie.setSecure(true);  // HTTPS에서만 전송
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(24 * 60 * 60);  // 24시간 유효

        log.info("로그인에 성공했습니다. 토큰을 발급합니다. Token: " + token);

        response.addCookie(jwtCookie);
        response.sendRedirect(frontendUrl + "/loginSuccess");
    }
}