package com.example.bookrating.service;

import com.example.bookrating.dto.BookDTO;
import com.example.bookrating.entity.Book;
import com.example.bookrating.repository.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@Slf4j
@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public BookDTO postBook(BookDTO bookDTO) {
        Book book = new Book();
        book.setIsbn(bookDTO.getIsbn());
        book.setTitle(bookDTO.getTitle());
        String isbn = book.getIsbn();
        /*bookRepository.findIsbn(isbn);
            log.info("중복 Isbn, data insert impossible");
*/
        if (bookRepository.findByIsbn(book.getIsbn()).isPresent()) {
            log.info("중복 Isbn, data insert impossible");
            //return; // 중복된 경우 더 이상 진행하지 않음
            //return 문: void 메서드에서 return 문은 단순히 메서드 실행을 종료하며, 에러를 발생시키지 않습니다.
            // HTTP 400 상태 코드와 JSON 에러 메시지 반환
            /*throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 존재하는 책입니다."); //409 error*/
            //ResponseStatusException을 사용하거나 @ControllerAdvice와 @ExceptionHandler를 사용하는 경우, 메서드에서 return 문을 따로 사용할 필요는 없습니다. 각 방식이 에러 응답을 클라이언트에게 자동으로 처리해 주기 때문입니다.
            return null;
        }

        bookRepository.save(book);

        return bookDTO;
    }
}
