package com.example.bookrating.dto;

import lombok.Data;

@Data
public class BookReviewsDTO {
    private Long bookId;
    private ReviewWithUserDTO reviews;
}
