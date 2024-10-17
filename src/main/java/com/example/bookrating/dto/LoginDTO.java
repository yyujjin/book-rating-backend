package com.example.bookrating.dto;

import lombok.Data;
import org.apache.catalina.User;

import java.util.List;

@Data
public class LoginDTO {
   private String accessToken;
   private UserDTO user;
}
