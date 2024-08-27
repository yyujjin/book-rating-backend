package com.example.bookrating.controller;

import com.example.bookrating.dto.BookDTO;
import com.example.bookrating.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookController {

    private static final Logger log = LoggerFactory.getLogger(BookController.class);
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }


    @PostMapping("/book")
    public void postBook(@ModelAttribute BookDTO bookDTO) {
        bookService.postBook(bookDTO);
        log.info("getIsbn : {}",bookDTO.getIsbn());
        log.info("getTitle : {}",bookDTO.getTitle());
    }

}
