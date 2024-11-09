package com.example.bookrating.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String providerId; //구글 고유아이디
    private String email; //이메일
    private String avatar; //이미지

   // private String name; //유저 이름
   // private String role; //권한

}
