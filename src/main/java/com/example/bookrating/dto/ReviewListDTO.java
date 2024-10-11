package com.example.bookrating.dto;

import lombok.Data;
import java.util.List;

@Data
public class ReviewListDTO {
    private List<ReviewDTO> reviews;
    private double average;
}
