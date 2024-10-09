package com.example.bookrating.dto;

import com.example.bookrating.entity.Review;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ReviewDTO {
   private List<Review> reviewList;
   private Map<String,Integer> average;
   private Map<String,List<Review>> reviews;


   public Map<String, List<Review>> setReviews(List<Review> reviewList){
       reviews = new HashMap<>();
       reviews.put("reviews",reviewList);

       return reviews;
   }


}
