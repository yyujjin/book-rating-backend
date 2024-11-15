package com.example.bookrating.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String providerId; //구글 고유 아이디
    private String role; //유저 권한
    //private String email;
    //private String avatar; //유저 사진

    // private String name;

}
