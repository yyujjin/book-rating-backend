package com.example.bookrating.dto;

import lombok.Data;

@Data
public class ReviewResponseDTO {
    private ReviewDetailDTO review;
    private double averageRating;
}
