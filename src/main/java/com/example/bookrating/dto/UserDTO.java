package com.example.bookrating.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String email;
    private String userName;
    private String avatar;
}
