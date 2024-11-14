package com.example.bookrating.repository;

import com.example.bookrating.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    //isbn으로 책 찾기
    Optional findByIsbn(String isbn);

    Page<Book> findAll(Pageable pageable);
}
