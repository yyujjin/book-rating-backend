package com.example.bookrating.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReviewDetailDTO {
    private Long id;
    private int rating;
    private String content;
    private LocalDateTime updatedAt;
    private UserDTO user;
}
