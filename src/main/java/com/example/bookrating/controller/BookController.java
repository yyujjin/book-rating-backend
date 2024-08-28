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
        BookDTO createBook =  bookService.postBook(bookDTO);
        log.info("getIsbn : {}",bookDTO.getIsbn());
        log.info("getTitle : {}",bookDTO.getTitle());
        //ResponseEntity는 Spring Framework에서 HTTP 응답을 구성할 때 사용하는 클래스입니다. 이를 통해 HTTP 상태 코드, 헤더, 본문(body) 등을 설정하여 클라이언트에게 응답을 전달할 수 있습니다.
        //ResponseEntity<BookDTO> 이건 ResponseEntity<BookDTO,HttpStatus> 이렇게 안해줘도 되는 이유는?
        //제네릭 타입: ResponseEntity<T>에서 T는 응답 본문에 포함될 데이터의 타입을 지정합니다. ResponseEntity<BookDTO>는 응답 본문이 BookDTO 타입임을 의미합니다.

                //상태 코드와 헤더 설정: 상태 코드와 헤더는 ResponseEntity 생성자에 전달할 수 있으며, 별도로 제네릭 타입으로 지정할 필요가 없습니다. 예를 들어, new ResponseEntity<>(body, HttpStatus.CREATED)에서는 body가 응답 본문이고 HttpStatus.CREATED가 상태 코드입니다.
                //상태 코드와 헤더는 ResponseEntity의 생성자 매개변수로 설정할 수 있습니다.

        if (createBook==null){
            return ResponseEntity.badRequest().body("이미 존재하는 책입니다.");
        }
        return ResponseEntity.ok().body(createBook);
    }

}
