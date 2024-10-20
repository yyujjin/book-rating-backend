package com.example.bookrating.service;

import com.example.bookrating.dto.CustomOAuth2User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserSessionService {




    // 현재 로그인한 사용자의 아이디를 반환
    public String getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            log.warn("인증 정보가 없습니다.");
            return null;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof CustomOAuth2User) {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) principal;
            String userId = oAuth2User.getUsername();
            return userId;
        }

        return null;
    }

    // 현재 로그인한 사용자의 이름을 반환
    public String getUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {

            return null;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof CustomOAuth2User) {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) principal;
            String userName = oAuth2User.getName();
            return userName;
        }
        return null;
    }


    // 현재 로그인한 사용자의 메일을 반환
    public String getUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof CustomOAuth2User) {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) principal;
            String userEmail = oAuth2User.getUserEmail();
            return userEmail;
        }
        return null;
    }


    // 현재 로그인한 사용자의 사진을 반환
    public String getAvatar() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof CustomOAuth2User) {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) principal;
            String avatar = oAuth2User.getAvatar();
            return avatar;
        }
        return null;
    }

}