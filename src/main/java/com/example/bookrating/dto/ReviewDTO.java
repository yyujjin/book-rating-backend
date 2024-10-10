package com.example.bookrating.dto;

import com.example.bookrating.entity.Review;
import lombok.Data;
import java.util.List;

@Data
public class ReviewDTO {
   private List<Review> reviews;
   private double average;
}
