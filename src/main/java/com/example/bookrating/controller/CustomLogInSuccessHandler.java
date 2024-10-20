package com.example.bookrating.controller;

import com.example.bookrating.service.UserSessionService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.IOException;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class CustomLogInSuccessHandler implements AuthenticationSuccessHandler {

    @Value("${frontend.url}")
    private String frontendUrl;

    @Value("${jwt.secret}")
    private String jwtSecret;

    private final UserSessionService userSessionService;

    public CustomLogInSuccessHandler(UserSessionService userSessionService) {
        this.userSessionService = userSessionService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException, java.io.IOException {

        // JWT 토큰 생성
        String token = Jwts.builder()
                .setSubject(authentication.getName()) // 사용자 정보
                .claim("authorities", authentication.getAuthorities()) // 권한 정보
                //.claim("id",)
                .claim("username", userSessionService.getUserName())
                .claim("email", userSessionService.getUserEmail())
                .claim("avatar", userSessionService.getAvatar())
                .setIssuedAt(new Date())
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

        // 쿠키를 응답에 추가. http header =  url?=querystring / body
       // cookie => header
             //   all header are saved to browser?
      //  only cookie
                //헤더중에 쿠키만 턱별히 저장이 돼!
        response.addCookie(jwtCookie);

        // 리다이렉트 할 경우: 성공 후 프론트엔드로 리다이렉트
        response.sendRedirect(frontendUrl + "/");
    }
}