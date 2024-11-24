package com.example.bookrating.service;

import com.example.bookrating.dto.*;
import com.example.bookrating.entity.Book;
import com.example.bookrating.entity.Review;
import com.example.bookrating.entity.UserEntity;
import com.example.bookrating.repository.BookRepository;
import com.example.bookrating.repository.ReviewRepository;
import com.example.bookrating.repository.UserRepository;
import com.example.bookrating.util.TokenExtractor;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;
    private final TokenExtractor tokenExtractor;
    private final UserRepository userRepository;

    public BookReviewsDTO getReviews(Long bookId, int page){

        Pageable pageable = PageRequest.of(page-1, 100, Sort.by("id").descending());
        Page<Review> reviewList= reviewRepository.findByBookId(bookId,pageable);


        if (reviewList.isEmpty()){
            BookReviewsDTO bookReviewsDTO = new BookReviewsDTO();
            bookReviewsDTO.setBookId(bookId);
            // 빈 리스트 설정
            bookReviewsDTO.setReviews(Collections.emptyList());

            return bookReviewsDTO;
        }

        List<ReviewWithUserDTO> reviewWithUserDTOS = new ArrayList<>();
        BookReviewsDTO responseDTO = new BookReviewsDTO();
        for (Review r : reviewList ){

            ReviewWithUserDTO review = new ReviewWithUserDTO();

            review.setId(r.getId());
            review.setRating(r.getRating());
            review.setContent(r.getContent());
            review.setUpdatedAt(r.getUpdatedAt());
            review.setUser(new UserDTO(r.getUserId(),r.getUsername()));

            responseDTO.setBookId(r.getBook().getId());
            reviewWithUserDTOS.add(review);
            responseDTO.setReviews(reviewWithUserDTOS);
        }
        return responseDTO;
    }

    //로그인한 사용자의 리뷰조회
    public ReviewDetailDTO getUserReviewByBookId(Long bookId, HttpServletRequest request) {
        //토큰에서 사용자 정보 추출
        UserDetailsDTO userDetailsDTO =  tokenExtractor.getUserInfoFromToken(request);

        //유저 아이디 빼내기
        Optional<UserEntity> user = userRepository.findByUsername(userDetailsDTO.getUsername());
        //사용자의 리뷰 전체 가져오기
        Optional<List<Review>> reviews = reviewRepository.getReviewsByUserId(user.get().getId());

        ReviewDetailDTO dto = new ReviewDetailDTO();
        for(Review r : reviews.get()) {
            if(r.getBook().getId().equals(bookId)) {
                dto.setId(r.getId());
                dto.setRating(r.getRating());
                dto.setContent(r.getContent());
                dto.setUpdatedAt(r.getUpdatedAt());
            }
        }
        if (dto.getId()==null){return null;}

        return dto;
    }

    public ReviewResponseDTO postReview(Long bookId, UserProfileReviewDTO userProfileReviewDTO, HttpServletRequest request){

        Review review = new Review();
        //토큰에서 유저 구글 고유아이디 가져옴
        UserDetailsDTO userDetailsDTO = tokenExtractor.getUserInfoFromToken(request);

        //guest 인 경우 (토큰 x)
        if(userDetailsDTO ==null) {
            review.setUserId(0L);
        }
        //로그인 유저 (토큰 O)
        if(userDetailsDTO !=null){
            //db에서 유저 정보 가져옴
            UserEntity user = userRepository.findByProviderId(userDetailsDTO.getProviderId());
            if(user!=null) {
                review.setUsername(user.getUsername());
                review.setUserId(user.getId());
                //유저 프로필 사진 저장
                //review.setUserAvatar(userDetailsDTO.getAvatar());
            }
            review.setRating(userProfileReviewDTO.getRating());
            review.setContent(userProfileReviewDTO.getContent());

            //todo:책 엔티티를 저장할지 책 id만 저장할지 생각해봐야할듯
            Optional<Book>book = bookRepository.findById(bookId);

            if (book.isEmpty()){return null;}
            review.setBook(book.get());
            review.prePersist();
            Review savedReview =  reviewRepository.save(review);

            ReviewResponseDTO reviewResponseDTO = new ReviewResponseDTO();
            ReviewDetailDTO reviewDetailDTO = new ReviewDetailDTO();
            reviewDetailDTO.setId(savedReview.getId());
            reviewDetailDTO.setContent(savedReview.getContent());
            reviewDetailDTO.setRating(savedReview.getRating());
            reviewDetailDTO.setUpdatedAt(savedReview.getUpdatedAt());
            reviewResponseDTO.setReview(reviewDetailDTO);
            reviewResponseDTO.setAverageRating(calculateAverage(getRatings(bookId)));
            return reviewResponseDTO;
        }
     return null;
    }

    //리뷰 존재 여부
    public Optional<Review> findById(Long reviewId){
        return reviewRepository.findById(reviewId);
    }

    //bookId, reviewId로 특정 리뷰 가져오기
    public Optional<Review> getReview(Long bookId,Long reviewId){
        return reviewRepository.findContentByBookIdAndReviewId(bookId,reviewId);
    }

    public Optional<Review> patchReview(Long bookId,Long reviewId,String content){
        //리뷰 가져오기
        Optional<Review> review =  getReview(bookId,reviewId);

        //Optional이 비어 있을 때 save 메서드가 호출되면 예외가 발생할 수 있기에 예외처리 해줘야함
        if ( review.isPresent()){
            review.get().setContent(content);
            reviewRepository.save(review.get());
        }

        //수정된 리뷰 또는 빈 Optional 반환
       return review;
    }

    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }

   //rating 가져오기
    public List<Integer> getRatings(Long bookId){
        return reviewRepository.getRatings(bookId);
    }

    //rating 평균점수 계산
    public double calculateAverage(List<Integer> ratings){
        int num = 0;
        for(int rate : ratings){
            num+= rate;
        }
       //소수점 2자리까지 출력
        try {
            return Math.round (num/ratings.size()*100) /100.0;
        }catch(ArithmeticException e){
            return  0;
        }
    }
}
