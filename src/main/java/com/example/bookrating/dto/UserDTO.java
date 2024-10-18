package com.example.bookrating.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String userName; //google + 제공자에서 발급해주는 아이디(번호)
    private String email; //이메일
    private String name; //유저 이름
    private String role; //권한
    private String avatar; //토큰
}
