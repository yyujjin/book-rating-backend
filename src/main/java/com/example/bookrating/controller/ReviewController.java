package com.example.bookrating.controller;

import com.example.bookrating.entity.Review;
import com.example.bookrating.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class ReviewController {

    @Autowired
    ReviewService reviewService;

    @GetMapping("/books/{bookId}/reviews")
    public ResponseEntity<List<Review>> getReviews(@PathVariable Long bookId) {
        return ResponseEntity.ok().body(reviewService.getReviews(bookId));
    }

    @PostMapping("/books/{bookId}/reviews")
    public ResponseEntity<?>postReview(@PathVariable Long bookId, @ModelAttribute Review review){
        boolean isReviewPosted = reviewService.postReview(bookId,review);
       if (isReviewPosted){
           return ResponseEntity.ok().body("리뷰가 등록되었습니다.");
       }
        return ResponseEntity.badRequest().body("리뷰 등록에 실패하였습니다.");
    }


}
