package com.example.bookrating.service;

import com.example.bookrating.entity.Book;
import com.example.bookrating.entity.Review;
import com.example.bookrating.repository.BookRepository;
import com.example.bookrating.repository.ReviewRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;
    private final BookService bookService;

    public ReviewService(ReviewRepository reviewRepository, BookRepository bookRepository, BookService bookService) {
        this.reviewRepository = reviewRepository;
        this.bookRepository = bookRepository;
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
}
