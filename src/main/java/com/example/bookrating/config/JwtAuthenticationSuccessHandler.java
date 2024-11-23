package com.example.bookrating.config;

import com.example.bookrating.dto.CustomOAuth2UserDTO;
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
import org.springframework.security.core.userdetails.UserDetails;
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
        String token = null;
        authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getPrincipal() instanceof CustomOAuth2UserDTO) {
            // OAuth2 로그인 사용자
            CustomOAuth2UserDTO customOAuth2UserDTO = (CustomOAuth2UserDTO) authentication.getPrincipal();
            System.out.println("OAuth2 사용자 정보: " + customOAuth2UserDTO.getAttributes());
            token = Jwts.builder()
                    .setSubject(customOAuth2UserDTO.getProviderId()) // 구글 고유 아이디
                    //.claim("authorities", authentication.getAuthorities()) // 권한 정보
                    // .claim("username", userSessionService.getUserName())
                    .claim("email", customOAuth2UserDTO.getUserEmail())
                    .claim("avatar", customOAuth2UserDTO.getAvatar())
                    .claim("username", customOAuth2UserDTO.getUsername())
                    .setIssuedAt(new Date()) //JWT 토큰이 발급된 시간
                    .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1일 유효
                    .signWith(SignatureAlgorithm.HS256, jwtSecret.getBytes()) // 서명 알고리즘
                    .compact();
        } else if (authentication.getPrincipal() instanceof UserDetails) {
            // 일반 로그인 사용자
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            System.out.println("일반 사용자 정보: " + userDetails.getUsername());
            token = Jwts.builder()
                    //.setSubject(userDetails.getUsername()) // 유저아이디
                    .claim("username",userDetails.getUsername())
                    .setIssuedAt(new Date()) //JWT 토큰이 발급된 시간
                    .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1일 유효
                    .signWith(SignatureAlgorithm.HS256, jwtSecret.getBytes()) // 서명 알고리즘
                    .compact();
        } else {
            // 인증되지 않은 상태 (예: AnonymousUser)
            System.out.println("인증되지 않은 사용자");
        }


        // JWT를 HttpOnly 쿠키에 저장
        Cookie jwtCookie = new Cookie("jwt", token);
        jwtCookie.setHttpOnly(true);  // 자바스크립트 접근 금지
        jwtCookie.setSecure(true);  // HTTPS에서만 전송
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(24 * 60 * 60);  // 24시간 유효

        log.info("로그인에 성공했습니다. 토큰을 발급합니다. Token: " + token);

        response.addCookie(jwtCookie);
        response.setStatus(201);
    }
}