package com.example.bookrating.service;

import com.example.bookrating.dto.BookDTO;
import com.example.bookrating.entity.Book;
import com.example.bookrating.repository.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// TODO 추가, 추가_중복체크, 수정, 삭제 테스트 코드 작성하기
// 작성법 모르겠음 스프링 입문 강의 참고
@Slf4j
@Service
public class BookService {

    private final BookRepository bookRepository;

    // TODO: Auto
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getBooks () {
        return bookRepository.findAll();
    }


    // TODO 함수는 하나의 일만 해야 함. 중복 체크하는 함수와 책 저장하는 함수 분리
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

    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    public Optional<Book> getBookByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }

    public Optional<Book> patchBook(Book book,String title){
        book.setTitle(title);
        bookRepository.save(book);
        return getBookById(book.getId());
    }

    // TODO 한가지 일만 하도록 함수 분리
    public boolean deleteBook(Long id) {

        if (bookRepository.findById(id).isPresent()){
            bookRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
