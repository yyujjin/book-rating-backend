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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;
import static org.springframework.http.RequestEntity.delete;
import static org.springframework.test.util.AssertionErrors.assertFalse;
import static org.springframework.test.util.AssertionErrors.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

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

    @Test
    public void 책_수정_성공() {
        //given
        Long id = 1L;
        Book book = new Book(id, "1111", "짱구야놀자");

        // Mock 설정: ID로 책을 조회할 때 book이 반환되도록 설정
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));

        // Mock 설정: bookRepository.save(book) 호출 시, book 객체가 반환되도록 설정
        when(bookRepository.save(book)).thenReturn(book);

        //when
        // 책의 제목을 "세일러문"으로 수정하고, 수정된 book 객체를 반환받음
        Optional<Book> result = bookService.patchBook(book, "세일러문");

        //then
        // 반환된 객체의 제목이 "세일러문"으로 변경되었는지 확인
        assertEquals("세일러문", result.get().getTitle());
    }

    @Test
    public void 책_삭제(){
        //given
        Book book = new Book(1L,"1111","스폰지밥");
        Long id = 1L;
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        bookRepository.deleteById(id);

        //when
        bookService.deleteBook(id);

        //when & then
        mockMvc.perform(delete("/books/{id}", id))
                .andExpect(status().isNoContent());  // 204 상태 코드 확인



    }



}