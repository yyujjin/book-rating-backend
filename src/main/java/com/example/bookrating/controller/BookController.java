package com.example.bookrating.controller;

import com.example.bookrating.dto.BookDTO;
import com.example.bookrating.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> postBook(@ModelAttribute BookDTO bookDTO) {
        log.info("getIsbn : {}",bookDTO.getIsbn());
        log.info("getTitle : {}",bookDTO.getTitle());

        boolean isBook =  bookService.postBook(bookDTO);

       if (isBook){
           return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 존재하는 책입니다.");
       }

        return ResponseEntity.status(HttpStatus.CREATED).body(bookDTO);
    }

}
