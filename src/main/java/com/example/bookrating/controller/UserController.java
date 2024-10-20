package com.example.bookrating.controller;

import com.example.bookrating.dto.LoginDTO;
import com.example.bookrating.dto.UserDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;


@RestController
@CrossOrigin
public class UserController {
    @Value("${jwt.secret}")
    private String SECRET_KEY;


    @PostMapping("/loginInfo")
    public ResponseEntity<LoginDTO> login(HttpServletRequest request){
        String token = getJwtFromCookies(request);  // 쿠키에서 JWT 가져오기
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();  // JWT가 없으면 401 응답
        }

        try {
            // JWT 토큰 파싱 및 Payload(Claims) 추출
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY.getBytes())  // 시크릿 키 설정
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // Payload에서 사용자 정보 추출
            String username = claims.get("username", String.class);
            String avatar = claims.get("avatar", String.class);

            // UserDTO 생성
            UserDTO userDTO = new UserDTO();
            userDTO.setId(1L);  // 예시로 사용 중인 id
            userDTO.setUserName(username);
            userDTO.setAvatar(avatar);

            // LoginDTO에 정보 추가
            LoginDTO loginDTO = new LoginDTO();
            loginDTO.setAccessToken(token);  // 클라이언트에 전달할 JWT 토큰
            loginDTO.setUser(userDTO);

            // 응답 반환
            return ResponseEntity.ok(loginDTO);

        } catch (JwtException e) {
            // JWT가 유효하지 않은 경우 처리
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
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
