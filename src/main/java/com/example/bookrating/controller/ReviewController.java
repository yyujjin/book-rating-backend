package com.example.bookrating.controller;

import com.example.bookrating.entity.Review;
import com.example.bookrating.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

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

    @PatchMapping("books/{bookId}/reviews/{reviewId}")
    public ResponseEntity<?>patchReview(@PathVariable Long bookId,@PathVariable Long reviewId,@RequestBody String content){
        //리뷰 찾기
        if (reviewService.findById(reviewId).isPresent()){
            //리뷰 수정 로직 작성
            Optional<Review> updatedReview = reviewService.patchReview(bookId,reviewId,content);
            return updatedReview
                    .map(review -> ResponseEntity.ok().body(review.getContent()))
                    .orElse(ResponseEntity.badRequest().body("리뷰 변경에 실패하였습니다."));
        }else {
            //존재하지 않는 리뷰
            return ResponseEntity.badRequest().body("존재하지 않는 리뷰입니다.");
        }
    }
}
