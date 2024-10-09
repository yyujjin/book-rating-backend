package com.example.bookrating.controller;

import com.example.bookrating.entity.Review;
import com.example.bookrating.service.BookService;
import com.example.bookrating.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
public class ReviewController {

    @Autowired
    ReviewService reviewService;

    @Autowired
    BookService bookService;

    @GetMapping("/books/{bookId}/reviews")
    public ResponseEntity<List<Review>> getReviews(@PathVariable Long bookId) {
        return ResponseEntity.ok().body(reviewService.getReviews(bookId));
    }

    @PostMapping("/books/{bookId}/reviews")
    public ResponseEntity<?> postReview(@PathVariable("bookId") Long bookId, @RequestBody Review review){
        //public ResponseEntity<?> postReview(@PathVariable("bookId") Long bookId, @RequestBody ReviewDTO reviewDTO){
       boolean isReviewPosted = reviewService.postReview(bookId,review);

       if (!isReviewPosted) return ResponseEntity.badRequest().body("리뷰 등록에 실패하였습니다.");

       if (review.getRating()>5) return ResponseEntity.badRequest().body("5이하의 별점만 가능합니다.");

        return ResponseEntity.ok().body("리뷰가 등록되었습니다.");
    }

    @PatchMapping("books/{bookId}/reviews/{reviewId}")
    public ResponseEntity<?>patchReview(@PathVariable Long bookId,@PathVariable Long reviewId,@RequestBody String content){

        //리뷰 찾기
        if (reviewService.findById(reviewId).isEmpty()){
            return ResponseEntity.badRequest().body("존재하지 않는 리뷰입니다.");
        }

        Optional<Review> updatedReview = reviewService.patchReview(bookId,reviewId,content);
        return updatedReview
                .map(review -> ResponseEntity.ok().body(review.getContent()))
                .orElse(ResponseEntity.badRequest().body("리뷰 변경에 실패하였습니다."));

    }

    @DeleteMapping("/books/{bookId}/reviews/{reviewId}")
    public ResponseEntity<?>deleteReview(@PathVariable Long bookId,@PathVariable Long reviewId){
        //없는 book
        if (bookService.getBookById(bookId).isEmpty()){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("존재하지 않는 책입니다");
        }

        //없는 review
        if (reviewService.findById(reviewId).isEmpty()){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("존재하지 않는 리뷰입니다.");
        }

        reviewService.deleteReview(reviewId);

        return ResponseEntity.ok().build();
    }

}
