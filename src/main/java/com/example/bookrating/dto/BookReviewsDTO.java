package com.example.bookrating.dto;

import lombok.Data;

import java.util.List;

@Data
public class BookReviewsDTO {
    private Long bookId;
    private List<ReviewWithUserDTO> reviews;
}
