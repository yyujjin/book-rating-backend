package com.example.bookrating.dto;

import java.util.Map;

public class GoogleResponseDTO {

    private final Map<String,Object> attribute;

    public GoogleResponseDTO(Map<String, Object> attribute) {
        this.attribute = attribute;
    }

    public String getProvider() {
        return "google";
    }

    public String getProviderId() {
        return attribute.get("sub").toString();
    }

    public String getEmail() {
        return attribute.get("email").toString();
    }

    public String getName() {
        return attribute.get("name").toString();
    }
}