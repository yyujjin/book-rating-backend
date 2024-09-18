package com.example.bookrating.repository;

import com.example.bookrating.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review,Long> {
    //책 기준 - 리뷰 가져오기
    List<Review> findByBookId(Long bookId);

    @Query("SELECT r FROM Review r WHERE r.book.id = :bookId AND r.id = :id")
    Optional<Review> findContentByBookIdAndReviewId(@Param("bookId") Long bookId, @Param("id") Long id);
}

