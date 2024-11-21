package com.example.bookrating.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserResponseDTO {
    private Long id;
    private String username;
    private List<UserProfileReviewDTO> reviews;
}
