package com.example.bookrating.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class CustomOAuth2UserDTO implements OAuth2User {

private final UserDetailsDTO userDetailsDTO;

    public CustomOAuth2UserDTO(UserDetailsDTO userDetailsDTO) {
        this.userDetailsDTO = userDetailsDTO;
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
                return userDetailsDTO.getRole();
            }
        });

        return collection;
    }

    // 사용자 식별자로 Google의 고유 ID 반환
    //밑에꺼랑 겹쳐서 빈문자열 반환할랬더니 에러남
    @Override
    public String getName() {
        return userDetailsDTO.getProviderId();
    }

    //유저 구글 고유 아이디
    public  String getProviderId(){
        return userDetailsDTO.getProviderId();
    }

    //유저 아이디
    public String getUsername() {return userDetailsDTO.getUsername();}
    //유저 이메일
    public String getUserEmail(){return userDetailsDTO.getEmail();}

    //유저 프로필 사진
    public String getAvatar(){return userDetailsDTO.getAvatar();}
}
