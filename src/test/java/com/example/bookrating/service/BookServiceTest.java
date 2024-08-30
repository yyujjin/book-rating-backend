package com.example.bookrating.service;

import com.example.bookrating.entity.Book;
import com.example.bookrating.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertFalse;
import static org.springframework.test.util.AssertionErrors.assertTrue;

public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    BookService bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void 중복_책_있음(){
        //given
        String isbn = "9781617294945";
        Book book = new Book(1L,isbn,"이상한 나라의 앨리스");
        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.of(book));
        
        //when
        boolean result = bookService.isDuplicateBook(isbn);
        
        //then
        assertTrue("중복 책있음",true);
        
    }

    @Test
    public void 중복_책_없음(){
        //given
        String isbn = "9781617294945";
        Book book = new Book(1L,isbn,"이상한 나라의 앨리스");
        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.of(book));

        //when
        boolean result = bookService.isDuplicateBook("123456789");

        //then
        assertFalse("중복책 없음",false);
    }



}
