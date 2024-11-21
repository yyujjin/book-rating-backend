package com.example.bookrating.controller;

import com.example.bookrating.dto.UserProfileReviewDTO;
import com.example.bookrating.dto.BookReviewsDTO;
import com.example.bookrating.dto.ReviewResponseDTO;
import com.example.bookrating.dto.ReviewWithUserDTO;
import com.example.bookrating.entity.Review;
import com.example.bookrating.repository.ReviewRepository;
import com.example.bookrating.repository.UserRepository;
import com.example.bookrating.service.BookService;
import com.example.bookrating.service.ReviewService;
import com.example.bookrating.util.TokenExtractor;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@CrossOrigin
public class ReviewController {

    private final ReviewService reviewService;
    private final BookService bookService;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final TokenExtractor tokenExtractor;

    @GetMapping("/books/{bookId}/reviews")
    public ResponseEntity<List<BookReviewsDTO>> getReviews(@PathVariable Long bookId, @RequestParam(defaultValue = "1") int page) {
        List<BookReviewsDTO> reviews =  reviewService.getReviews(bookId,page);

        if (reviews.isEmpty()) {return ResponseEntity.noContent().build();}

        return ResponseEntity.ok().body(reviewService.getReviews(bookId,page));
    }

    //로그인한 사용자의 리뷰 조회
    @GetMapping("/books/{bookId}/reviews/my-review")
    public ResponseEntity<?> getReviewsByBookId(@PathVariable("bookId") Long bookId, HttpServletRequest request){

        List<ReviewWithUserDTO> review = reviewService.getUserReviewByBookId(bookId,request);
        if(review.isEmpty()){return ResponseEntity.status(204).build();}

        return ResponseEntity.ok().body(review);
    }

    @PostMapping("/books/{bookId}/reviews")
    public ResponseEntity<?> postReview(@PathVariable("bookId") Long bookId, @RequestBody UserProfileReviewDTO userProfileReviewDTO, HttpServletRequest request){

        if (userProfileReviewDTO.getRating()<0|| userProfileReviewDTO.getRating()>5) return ResponseEntity.badRequest().body("0이상 5이하의 별점만 가능합니다.");
        if (userProfileReviewDTO.getContent().isBlank())return ResponseEntity.badRequest().body("내용을 입력해주세요!");

        ReviewResponseDTO postedReview = reviewService.postReview(bookId, userProfileReviewDTO,request);

        if (postedReview==null) { return ResponseEntity.badRequest().body("리뷰 등록에 실패하였습니다.");}

        return ResponseEntity.ok().body(postedReview);
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
