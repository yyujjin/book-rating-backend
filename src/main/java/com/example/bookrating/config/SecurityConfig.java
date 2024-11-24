package com.example.bookrating.config;

import com.example.bookrating.service.CustomOAuth2UserService;
import com.example.bookrating.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final JwtAuthenticationSuccessHandler jwtAuthenticationSuccessHandler;
    private final JwtUtil jwtUtil;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        // AuthenticationManager를 HttpSecurity에서 빌드하여 빈으로 등록
        return http.getSharedObject(AuthenticationManagerBuilder.class).build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        JsonAuthenticationFilter jsonAuthenticationFilter = new JsonAuthenticationFilter("/auth/login", authenticationManager);
        jsonAuthenticationFilter.setAuthenticationSuccessHandler(jwtAuthenticationSuccessHandler);

        http
                .csrf((auth) -> auth.disable())
                .formLogin((auth) -> auth.disable())
                .httpBasic((auth) -> auth.disable())

                //세션 설정 : STATELESS -> 요청마다 JWT로 인증 상태를 확인하는 무상태 인증 방식으로 설정
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                //OAuth2 인증 중 사용자 정보를 불러오고 저장
                .oauth2Login((oauth2)->oauth2
                        .userInfoEndpoint((userInfoEndpointConfig ->userInfoEndpointConfig
                        .userService(customOAuth2UserService)))
                        .successHandler(jwtAuthenticationSuccessHandler)
                )

                //경로별 인가 작업
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/tags","/auth/register","/auth/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/books/*/reviews","/books","/books/{id}").permitAll() //get에 한해서만 적용
                        .anyRequest().authenticated()
                )

                // 인증되지 않은 사용자에 대해 로그인페이지로 리다이렉트가 아닌 401 상태 코드 반환
                .exceptionHandling((exceptions) -> exceptions
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                )

                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .deleteCookies("JSESSIONID", "jwt")  //JSESSIONID: 세션 식별 쿠키/jwt : 토큰을 저장하는 쿠키 삭제
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.setStatus(HttpServletResponse.SC_OK);  //리다이렉트 없이 직접 응답 처리 :  200 상태 코드 설정
                            response.getWriter().flush();  // 응답 본문 비우기 (필요 시 메시지 추가 가능)
                        })
                )
                .addFilterBefore(jsonAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new JwtAuthenticationFilter(jwtUtil), JsonAuthenticationFilter.class);

        return http.build();
    }
}