package com.example.bookrating.controller;

import com.example.bookrating.dto.BookDTO;
import com.example.bookrating.entity.Book;
import com.example.bookrating.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class BookController {

    private static final Logger log = LoggerFactory.getLogger(BookController.class);
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }


    @GetMapping("/books")
    public ResponseEntity<?> getBooks() {
        return  ResponseEntity.ok().body(bookService.getBooks());
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

    @PatchMapping("/books/{id}")
    public ResponseEntity<?> patchBook(@PathVariable Long id,@RequestBody String title) {
        Optional<Book> findBook = bookService.getBook(id);
        if (findBook.isEmpty()){
            return  ResponseEntity.badRequest().body("존재하지 않는 책입니다");
        }
        bookService.patchBook(findBook.get(),title);
        return ResponseEntity.ok(). body(bookService.patchBook(findBook.get(),title));
    }

}
