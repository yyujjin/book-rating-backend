package com.example.bookrating.controller;

import com.example.bookrating.dto.BookRequestDTO;
import com.example.bookrating.dto.BookResponseDetailDTO;
import com.example.bookrating.dto.BookResponseSummaryDTO;
import com.example.bookrating.entity.Book;
import com.example.bookrating.service.BookService;
import com.example.bookrating.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@CrossOrigin
@Tag(name = "Book", description = "책 관리 API")
public class BookController {

    private static final Logger log = LoggerFactory.getLogger(BookController.class);
    private final BookService bookService;
    private final ReviewService reviewService;

    @GetMapping("/books/{id}")
    @Operation(summary = "책 조회", description = "ID로 특정 책을 조회합니다.")
    public ResponseEntity<?>getBook(@PathVariable Long id) {

        if(id==null || id<1) {return ResponseEntity.status(400).body("ID는 필수 입력값이며, 양수이어야 합니다."); }
        BookResponseDetailDTO gotBook = bookService.getBook(id);
        if (gotBook == null) {return ResponseEntity.status(404).body("요청한 ID의 책이 존재하지 않습니다.");}

        return  ResponseEntity.ok(gotBook);
    }

    @GetMapping("/books")
    @Operation(summary = "책 목록 조회", description = "페이지 번호를 기준으로 책 목록을 100개씩 조회합니다.")
    public ResponseEntity<List<BookResponseSummaryDTO>> getBooks(@RequestParam(defaultValue = "1") int page) {

        return  ResponseEntity.ok().body(bookService.getBooksByPaging(page));
    }

    @PostMapping("/books")
    @Operation(summary = "책 생성", description = "새로운 책을 등록합니다. ISBN이 중복되면 409 상태 코드를 반환합니다.",
            security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> createBook(@RequestBody BookRequestDTO bookDTO) {

        if (bookService.isDuplicateBook(bookDTO.getIsbn())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 존재하는 책입니다.");
        }

        BookResponseDetailDTO createdBook =  bookService.createBook(bookDTO);

        return ResponseEntity.status(201).body(createdBook);
    }

    @PatchMapping("/books/{id}")
    @Operation(summary = "책 정보 수정", description = "ID로 책을 조회한 후, 제목을 수정합니다. 존재하지 않는 책이면 400 상태 코드를 반환합니다.",
            security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> editBook(@PathVariable Long id,@RequestBody String title) {
        Optional<Book> findBook = bookService.getBookById(id);
        if (findBook.isEmpty()){
            return  ResponseEntity.badRequest().body("존재하지 않는 책입니다");
        }

        Optional<Book> modifiedBook =  bookService.editBook(findBook.get(),title);

        return ResponseEntity.ok(). body(modifiedBook);
    }

    @DeleteMapping("/books/{id}")
    @Operation(summary = "책 삭제", description = "ID로 책을 조회한 후 삭제합니다. 삭제하려는 책에 리뷰가 존재하면 400 상태 코드를 반환합니다.",
            security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {

        if (bookService.getBookById(id).isPresent()){

            //삭제하고자 하는 책에 리뷰 존재시 400 에러반환
            if (bookService.findReview(id))  { return ResponseEntity.badRequest().build();}

            bookService.deleteBook(id);
            return  ResponseEntity.ok().build();
        }

        return ResponseEntity.badRequest().body("존재하지 않는 책입니다.");
    }
}