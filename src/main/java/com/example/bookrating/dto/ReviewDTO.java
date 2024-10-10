package com.example.bookrating.dto;

import lombok.Data;
import java.util.List;

@Data
public class ReviewDTO {
   private List<?> reviews;
   private double average;
   private String content;
   private int rating;
}
