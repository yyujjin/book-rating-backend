package com.example.bookrating.service;

import com.example.bookrating.entity.Book;
import com.example.bookrating.entity.Review;
import com.example.bookrating.repository.ReviewRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookService bookService;

    public ReviewService(ReviewRepository reviewRepository, BookService bookService) {
        this.reviewRepository = reviewRepository;
        this.bookService = bookService;
    }

    public List<Review>getReviews(Long bookId){
        log.info("넘어온 값 : {}",reviewRepository.findByBookId(bookId));
        return reviewRepository.findByBookId(bookId);
    }

    public boolean postReview(Long bookId, Review review){
        log.info("bookId:{}",bookId,"review:{}",review);
        Optional<Book>book = bookService.getBookById(bookId);
        if (book.isEmpty()){return false;}
        review.setBook(book.get());
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
}
