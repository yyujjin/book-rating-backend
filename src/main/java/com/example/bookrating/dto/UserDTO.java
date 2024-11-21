package com.example.bookrating.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String password;

    public UserDTO() {
    }

    public UserDTO(Long id, String password) {
        this.id = id;
        this.password = password;
    }
}
