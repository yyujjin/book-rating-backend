package com.example.bookrating.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReviewDetailDTO {
    private Long id;
    private String content;
    private int rating;
    private LocalDateTime updatedAt;
}
