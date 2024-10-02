package com.example.bookrating.repository;

import com.example.bookrating.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    //isbn으로 책 찾기
    Optional findByIsbn(String isbn);

    //book average 업데이트
    @Modifying
    @Query("UPDATE Book b SET b.average = :average WHERE b.id = :bookId")
    int updateAverageRating(@Param("bookId") Long bookId, @Param("average") double average);

}
