package com.example.bookrating.config;

import com.example.bookrating.controller.CustomLogInSuccessHandler;
import com.example.bookrating.controller.CustomLogoutHandler;
import com.example.bookrating.service.CustomOAuth2UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    // application.properties 또는 .env 파일에서 FRONTEND_URL 값을 가져옴
    @Value("${frontend.url}")
    private String frontendUrl;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomLogInSuccessHandler customLogInSuccessHandler;
    private final CustomLogoutHandler customLogoutHandler;
    private final JwtUtil jwtUtil;

    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService, CustomLogInSuccessHandler customLogInSuccessHandler, CustomLogoutHandler customLogoutHandler, JwtUtil jwtUtil) {
        this.customOAuth2UserService = customOAuth2UserService;
        this.customLogInSuccessHandler = customLogInSuccessHandler;
        this.customLogoutHandler = customLogoutHandler;
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        //csrf disable
        http
                .csrf((auth) -> auth.disable());

        //From 로그인 방식 disable
        http
                .formLogin((auth) -> auth.disable());

        //HTTP Basic 인증 방식 disable
        http
                .httpBasic((auth) -> auth.disable());

        //oauth2
        http
                .oauth2Login((oauth2)->oauth2
                        .userInfoEndpoint((userInfoEndpointConfig ->userInfoEndpointConfig
                                .userService(customOAuth2UserService))));

        //경로별 인가 작업
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/books","/tags","/loginInfo").permitAll()
                        .requestMatchers(HttpMethod.GET, "/books/*/reviews").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling((exceptions) -> exceptions
                        // 인증되지 않은 사용자에 대해 로그인페이지로 리다이렉트가 아닌 401 상태 코드 반환
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                )
                .oauth2Login(oauth2 -> oauth2
                        .defaultSuccessUrl(frontendUrl+"/loginSuccess", true) // 로그인 성공 후 리다이렉트될 프론트엔드 경로
                        .failureUrl(frontendUrl+"/loginSuccess?error=true") // 로그인 실패 시 리다이렉트될 프론트엔드 경로
                        .successHandler(customLogInSuccessHandler)

                )
                .logout(logout -> logout
                        .logoutUrl("/logout")  // 로그아웃 처리할 URL 설정
                        //.addLogoutHandler(customLogoutHandler)  // 커스텀 로그아웃 핸들러 추가
                        .invalidateHttpSession(true)  // 세션 무효화
                        .deleteCookies("JSESSIONID", "jwt")  //JSESSIONID: 세션 식별 쿠키/jwt : 토큰을 저장하는 쿠키 삭제
                        .logoutSuccessHandler((request, response, authentication) -> {
                            // 리다이렉트 없이 직접 응답 처리
                            response.setStatus(HttpServletResponse.SC_OK);  // 200 상태 코드 설정
                            response.getWriter().flush();  // 응답 본문 비우기 (필요 시 메시지 추가 가능)
                        })
                )

               /* .exceptionHandling(exceptions -> exceptions
                        // loginInfo 경로에 대해서는 기본 401 에러 반환
                        .defaultAuthenticationEntryPointFor(
                                new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                                new AntPathRequestMatcher("/loginInfo")
                        )
                )*/
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class); // JWT 필터 추가

        //세션 설정 : STATELESS
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
