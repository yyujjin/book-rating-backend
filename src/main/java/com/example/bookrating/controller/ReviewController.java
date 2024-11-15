package com.example.bookrating.controller;

import com.example.bookrating.dto.ReviewDTO;
import com.example.bookrating.dto.ReviewListDTO;
import com.example.bookrating.dto.UserDTO;
import com.example.bookrating.entity.Review;
import com.example.bookrating.entity.UserEntity;
import com.example.bookrating.repository.UserRepository;
import com.example.bookrating.service.BookService;
import com.example.bookrating.service.ReviewService;
import com.example.bookrating.util.TokenExtractor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@CrossOrigin
public class ReviewController {

    @Autowired
    ReviewService reviewService;

    @Autowired
    BookService bookService;

    @Autowired
    TokenExtractor tokenExtractor;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/books/{bookId}/reviews")
    public ResponseEntity<ReviewListDTO> getReviews(@PathVariable Long bookId, @RequestParam(defaultValue = "1") int page) {
      return ResponseEntity.ok().body(reviewService.getReviews(bookId,page));
    }

    @PostMapping("/books/{bookId}/reviews")
    public ResponseEntity<?> postReview(@PathVariable("bookId") Long bookId, @RequestBody ReviewDTO reviewDTO, HttpServletRequest request){

        if (reviewDTO.getRating()<0||reviewDTO.getRating()>5) return ResponseEntity.badRequest().body("0이상 5이하의 별점만 가능합니다.");
        if (reviewDTO.getContent().isBlank())return ResponseEntity.badRequest().body("내용을 입력해주세요!");
        Review review = new Review();
        //토큰에서 유저 구글 고유아이디 가져옴
        UserDTO userDTO = tokenExtractor.getUserInfoFromToken(request);

        //guest 인 경우 (토큰 x)
        if(userDTO==null) {
            review.setUserId(0L);
        }
        //로그인 유저 (토큰 O)
        if(userDTO!=null){
            //db에서 유저 정보 가져옴
            UserEntity user = userRepository.findByProviderId(userDTO.getProviderId());
            //유저 고유 아이디 저장
            if(user!=null)  review.setUserId(user.getId());
            //유저 프로필 사진 저장
            review.setUserAvatar(userDTO.getAvatar());
        }
        review.setRating(reviewDTO.getRating());
        review.setContent(reviewDTO.getContent());

        boolean isReviewPosted = reviewService.postReview(bookId,review);
        if (!isReviewPosted) return ResponseEntity.badRequest().body("리뷰 등록에 실패하였습니다.");

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
