package com.example.bookrating.service;

import com.example.bookrating.dto.*;
import com.example.bookrating.entity.Book;
import com.example.bookrating.entity.Review;
import com.example.bookrating.repository.BookRepository;
import com.example.bookrating.repository.ReviewRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;

    public ReviewService(ReviewRepository reviewRepository, BookRepository bookRepository) {
        this.reviewRepository = reviewRepository;
        this.bookRepository = bookRepository;
    }

    public List<BookReviewsDTO> getReviews(Long bookId, int page){

       // ReviewListDTO reviewListDTO = new ReviewListDTO();
        //List<ReviewDTO> reviewDTOList = new ArrayList<>();

        List<BookReviewsDTO> bookReviewsDTOList = new ArrayList<>();



        Pageable pageable = PageRequest.of(page-1, 100, Sort.by("id").descending());
        Page<Review> reviewList= reviewRepository.findByBookId(bookId,pageable);


        for (Review r : reviewList ){
           // ReviewDTO dto = new ReviewDTO(review.getId(),review.getBook().getId(), review.getRating(),review.getContent(),review.getUpdatedAt(),review.getUserId(),review.getUserAvatar());
            //reviewDTOList.add(dto);

            ReviewDetailDTO review = new ReviewDetailDTO();

            review.setId(r.getId());
            review.setRating(r.getRating());
            review.setContent(r.getContent());
            review.setUpdatedAt(r.getUpdatedAt());
            //일단 임시로 아바타 넣음 원래는 유저네임 넣어야함
            review.setUser(new UserDTO(r.getUserId(),r.getUsername()));
            BookReviewsDTO responseDTO = new BookReviewsDTO();
            responseDTO.setBookId(r.getBook().getId());
            responseDTO.setReviews(review);

            bookReviewsDTOList.add(responseDTO);
        }

        //평균 조회
       // reviewListDTO.setAverageRating(calculateAverage(getRatings(bookId)));

        return bookReviewsDTOList;
    }

    public boolean postReview(Long bookId, Review review){
        log.info("bookId:{}",bookId,"review:{}",review);
        Optional<Book>book = bookRepository.findById(bookId);

        if (book.isEmpty()){return false;}
        review.setBook(book.get());
        review.prePersist();
        reviewRepository.save(review);
        return true;
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
            log.info("0을 나눌 수 없습니다. ");
        };

        return  0;
    }
}
