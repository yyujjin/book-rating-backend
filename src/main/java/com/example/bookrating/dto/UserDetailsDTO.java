package com.example.bookrating.dto;

import lombok.Data;

@Data
public class UserDetailsDTO {
    private Long id;
    private String providerId; //구글 고유아이디
    private String username; //일반 유저 아이디
    private String password;
    private String email; //이메일
    private String avatar; //이미지
    private String role; //권한
   // private String name; //유저 이름


}
