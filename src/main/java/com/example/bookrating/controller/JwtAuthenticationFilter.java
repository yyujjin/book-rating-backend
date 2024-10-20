package com.example.bookrating.controller;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Cookie;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    private AntPathMatcher pathMatcher = new AntPathMatcher();


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {


        // 1. 토큰이 필요하지 않은 API URL에 대해서 배열로 구성한다.
        List<String> list = Arrays.asList(
                "/login",  // 로그인 페이지의 URL을 추가합니다.
                "/books",
                "/books/*/reviews"
        );

        // 2. 토큰이 필요하지 않은 API URL의 경우 -> 로직 처리없이 다음 필터로 이동한다.
        if (list.contains(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        for (String pattern : list) {
            if (pathMatcher.match(pattern, request.getRequestURI())) {
                filterChain.doFilter(request, response);
                return;
            }
        }

        // 쿠키에서 JWT 토큰 추출
        String token = getJwtFromCookies(request);

        if (token != null) {
            try {
//                return Jwts
//                        .parserBuilder()
//                        .setSigningKey(key)
//                        .build()
//                        .parseClaimsJws(token)
//                        .body
                // 토큰의 서명과 유효성 검증
                Jwts.parser()
                        .setSigningKey(SECRET_KEY.getBytes())  // 시크릿 키 설정
                        .build()
                        .parseSignedClaims(token);  // JWT 토큰의 유효성 확인

                // 토큰이 유효하면 필터 체인 계속 진행
                filterChain.doFilter(request, response);

            } catch (JwtException e) {
                // 토큰이 유효하지 않거나 만료된 경우
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);  // 401 응답
                response.getWriter().write("Invalid or expired token");
                return;
            }

        } else {
            // 쿠키에 토큰이 없으면 필터 체인 계속 진행
            filterChain.doFilter(request, response);
        }
    }

    // 쿠키에서 "jwt"라는 이름의 쿠키 값을 가져옴
    private String getJwtFromCookies(HttpServletRequest request) {
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