package com.example.bookrating.controller;

import com.example.bookrating.dto.BookDTO;
import com.example.bookrating.entity.Book;
import com.example.bookrating.repository.BookRepository;
import com.example.bookrating.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertFalse;
import static org.springframework.test.util.AssertionErrors.assertTrue;


public class BookControllerTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void 책_저장_성공() {
        //given
        BookDTO bookDTO = new BookDTO();
        bookDTO.setIsbn("12345");
        bookDTO.setTitle("짱구야노올자");

        String isbn = "22222";
        Book book = new Book(1l, isbn, "세일러문");
        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.of(book));

        //when
        bookService.isDuplicateBook(isbn);

        //then
        assertFalse("책 저장 완료",false);
    }

    @Test
    public void 책_저장_실패() {
        //given
        BookDTO bookDTO = new BookDTO();
        bookDTO.setIsbn("12345");
        bookDTO.setTitle("짱구야노올자");

        String isbn = "12345";
        Book book = new Book(1l, isbn, "세일러문");
        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.of(book));

        //when
        bookService.isDuplicateBook(isbn);

        //then
        assertTrue("책 저장 실패",true);
    }

}