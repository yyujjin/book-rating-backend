package com.example.bookrating.dto;

import java.util.Map;

public class GoogleResponseDTO {
    private final Map<String, Object> attribute;


    public GoogleResponseDTO(Map<String, Object> attribute) {

        this.attribute = attribute;
    }

    //제공자 (Ex. naver, google, ...)
    public String getProvider() {

        return "google";
    }

    //제공자에서 발급해주는 아이디(번호)
    public String getProviderId() {

        return attribute.get("sub").toString();
    }

    //이메일
    public String getEmail() {

        return attribute.get("email").toString();
    }

    //사용자 실명 (설정한 이름)
    public String getName() {

        return attribute.get("name").toString();
    }
}
