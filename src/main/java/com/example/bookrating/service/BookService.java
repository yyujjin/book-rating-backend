package com.example.bookrating.service;

import com.example.bookrating.dto.BookDTO;
import com.example.bookrating.entity.Book;
import com.example.bookrating.repository.BookRepository;
import org.springframework.stereotype.Service;


@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public void postBook(BookDTO bookDTO) {
        Book book = new Book();
        book.setIsbn(bookDTO.getIsbn());
        book.setTitle(bookDTO.getTitle());
        bookRepository.save(book);
    }
}
