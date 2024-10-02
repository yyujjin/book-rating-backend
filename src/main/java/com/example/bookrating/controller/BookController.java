package com.example.bookrating.controller;

import com.example.bookrating.dto.BookDTO;
import com.example.bookrating.entity.Book;
import com.example.bookrating.service.BookService;
import com.example.bookrating.service.ReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class BookController {

    private static final Logger log = LoggerFactory.getLogger(BookController.class);

    @Autowired
    BookService bookService;

    @Autowired
    ReviewService reviewService;

    @GetMapping("/books")
    public ResponseEntity<List<Book>> getBooks() {

        List<Book>  bookList = bookService.getBooks();

        for (Book book :  bookList){
            book.setAverage( reviewService.calculateAverage(reviewService.getRatings(book.getId())));
            bookService.updateAverage(book);
        }

        return  ResponseEntity.ok().body(bookService.getBooks());
    }

    @PostMapping("/books")
    public ResponseEntity<?> createBook(@RequestBody BookDTO bookDTO) {
        log.info("getIsbn : {}",bookDTO.getIsbn());
        log.info("getTitle : {}",bookDTO.getTitle());
        log.info("getTags : {}",bookDTO.getTagIds());

        if (bookService.isDuplicateBook(bookDTO.getIsbn())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 존재하는 책입니다.");
        }

        bookService.createBook(bookDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.getBookByIsbn(bookDTO.getIsbn()));
    }

    @PatchMapping("/books/{id}")
    public ResponseEntity<?> editBook(@PathVariable Long id,@RequestBody String title) {
        Optional<Book> findBook = bookService.getBookById(id);
        if (findBook.isEmpty()){
            return  ResponseEntity.badRequest().body("존재하지 않는 책입니다");
        }

        Optional<Book> modifiedBook =  bookService.editBook(findBook.get(),title);

        return ResponseEntity.ok(). body(modifiedBook);
    }

    @DeleteMapping("/books/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        if (bookService.getBookById(id).isPresent()){
            bookService.deleteBook(id);
            return  ResponseEntity.noContent().build();
        }
        return ResponseEntity.badRequest().body("존재하지 않는 책입니다.");
    }
}
