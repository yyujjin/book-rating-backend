package com.example.bookrating.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReviewDTO {
   private Long id;
   private int rating;
   private String content;
   private LocalDateTime updatedAt;

   public ReviewDTO(Long id,int rating, String content, LocalDateTime updatedAt) {
      this.id = id;
      this.rating = rating;
      this.content = content;
      this.updatedAt = updatedAt;
   }
}
