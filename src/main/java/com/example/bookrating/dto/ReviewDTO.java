package com.example.bookrating.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReviewDTO {
   private Long id;
   private int rating;
   private String content;
   private LocalDateTime updatedAt;
   private Long userId; //유저 고유 아이디
   private String userAvatar; //유저 프로필 사진

   public ReviewDTO(Long id,int rating, String content, LocalDateTime updatedAt,Long userId,String userAvatar) {
      this.id = id;
      this.rating = rating;
      this.content = content;
      this.updatedAt = updatedAt;
      this.userId = userId;
      this.userAvatar = userAvatar;
   }


}
