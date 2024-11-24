package com.example.bookrating.controller;

import com.example.bookrating.dto.*;
import com.example.bookrating.entity.Review;
import com.example.bookrating.repository.ReviewRepository;
import com.example.bookrating.repository.UserRepository;
import com.example.bookrating.service.BookService;
import com.example.bookrating.service.ReviewService;
import com.example.bookrating.util.TokenExtractor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
@CrossOrigin
@Tag(name = "Review", description = "리뷰 관리 API")
public class ReviewController {

    private final ReviewService reviewService;
    private final BookService bookService;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final TokenExtractor tokenExtractor;

    @GetMapping("/books/{bookId}/reviews")
    @Operation(summary = "책 리뷰 조회", description = "특정 책에 대한 리뷰 목록을 페이지 단위로 100개씩 조회합니다.")
    public ResponseEntity<BookReviewsDTO> getReviews(@PathVariable Long bookId, @RequestParam(defaultValue = "1") int page) {
        BookReviewsDTO review =  reviewService.getReviews(bookId,page);

        return ResponseEntity.ok().body(review);
    }


    //로그인한 사용자의 특정 책에 대한 리뷰 조회(하나만 반환)
    @GetMapping("/books/{bookId}/reviews/my-review")
    @Operation(summary = "사용자의 리뷰 조회", description = "로그인한 사용자가 특정 책에 남긴 리뷰를 조회합니다.")
    public ResponseEntity<?> getReviewsByBookId(@PathVariable("bookId") Long bookId, HttpServletRequest request){

        ReviewDetailDTO review = reviewService.getUserReviewByBookId(bookId,request);
        if(review==null){return ResponseEntity.notFound().build();}

        return ResponseEntity.ok().body(review);
    }

    @PostMapping("/books/{bookId}/reviews")
    @Operation(summary = "리뷰 등록", description = "특정 책에 대해 별점과 내용을 포함한 리뷰를 등록합니다. 별점은 0에서 5 사이만 가능합니다.")
    public ResponseEntity<?> postReview(@PathVariable("bookId") Long bookId, @RequestBody UserProfileReviewDTO userProfileReviewDTO, HttpServletRequest request){

        if (userProfileReviewDTO.getRating()<0|| userProfileReviewDTO.getRating()>5) return ResponseEntity.badRequest().body("0이상 5이하의 별점만 가능합니다.");
        if (userProfileReviewDTO.getContent().isBlank())return ResponseEntity.badRequest().body("내용을 입력해주세요!");

        ReviewResponseDTO postedReview = reviewService.postReview(bookId, userProfileReviewDTO,request);

        if (postedReview==null) { return ResponseEntity.badRequest().body("리뷰 등록에 실패하였습니다.");}

        return ResponseEntity.ok().body(postedReview);
    }

    @PatchMapping("books/{bookId}/reviews/{reviewId}")
    @Operation(summary = "리뷰 수정", description = "특정 책과 리뷰 ID를 기반으로 리뷰 내용을 수정합니다.")
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
    @Operation(summary = "리뷰 삭제", description = "특정 책과 리뷰 ID를 기반으로 리뷰를 삭제합니다. 존재하지 않는 책이나 리뷰일 경우 에러 메시지를 반환합니다.")
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
