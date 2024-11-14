package com.example.bookrating.repository;

import com.example.bookrating.entity.Book;
import com.example.bookrating.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review,Long> {
    //책 기준 - 리뷰 가져오기
    //List<Review> findByBookId(Long bookId);

    //페이징 처리
    Page<Review> findByBookId(Long bookId,Pageable pageable);

    @Query("SELECT r FROM Review r WHERE r.book.id = :bookId AND r.id = :id")
    Optional<Review> findContentByBookIdAndReviewId(@Param("bookId") Long bookId, @Param("id") Long id);

    //rating 점수 가져오기
    @Query("SELECT rating FROM Review r WHERE r.book.id = :bookId")
    List<Integer> getRatings(@Param("bookId") Long bookId);
}

