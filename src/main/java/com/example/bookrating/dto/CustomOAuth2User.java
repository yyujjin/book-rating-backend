package com.example.bookrating.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {

private final UserDTO userDTO;

    public CustomOAuth2User(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of();
    }

    //유저 권한
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority>collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return userDTO.getRole();
            }
        });

        return collection;
    }

    //유저 이름
    @Override
    public String getName() {
        return userDTO.getName();
    }

    //유저 아이디
    public  String getUsername(){
        return userDTO.getUserName();
    }

    //유저 이메일
    public String getUserEmail(){return userDTO.getEmail();}

    //유저 프로필 사진
    public String getAvatar(){return userDTO.getAvatar();}
}
