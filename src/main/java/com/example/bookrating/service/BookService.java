package com.example.bookrating.service;

import com.example.bookrating.dto.BookDTO;
import com.example.bookrating.entity.Book;
import com.example.bookrating.repository.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getBooks () {
        return bookRepository.findAll();
    }



    public boolean postBook(BookDTO bookDTO) {
        Book book = new Book();
        book.setIsbn(bookDTO.getIsbn());
        book.setTitle(bookDTO.getTitle());
        String isbn = book.getIsbn();

    if ( bookRepository.findByIsbn(book.getIsbn()).isPresent()) {
        log.info("중복 Isbn");
        return true;
    }else {
        bookRepository.save(book);
        log.info("책 저장 완료!");
    }
        return false;
    }
}
