package com.example.bookrating.repository;

import com.example.bookrating.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    /*void findIsbn(String isbn);*/

    Optional<Object> findByIsbn(String isbn);
}
