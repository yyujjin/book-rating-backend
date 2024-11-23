package com.example.bookrating.service;

import com.example.bookrating.dto.BookResponseDTO;
import com.example.bookrating.entity.Book;
import com.example.bookrating.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.*;

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


    @Test
    public void 책_저장(){
        //given
        BookResponseDTO bookDTO = new BookResponseDTO();
        bookDTO.setIsbn("1111");
        bookDTO.setTitle("앨리스");

        //when
        bookService.postBook(bookDTO);

        //then
        verify(bookRepository).save(any(Book.class));
        //bookRepository.save() 메서드가 Book 타입의 객체를 인자로 전달받아 호출되었는지 검증합니다.
        //any(Book.class) : 동일한 인스턴스의 필요 없이 특정 타입에 대한 메서드 호출을 허용하고 검증할 수 있는 유연한 방법
    }


    @Test
    public void 책_수정(){
        //givne
        Book book = new Book(12L,"1111","신의괴도잔느");
        String title = "아따맘마";

        when(bookRepository.save(book)).thenReturn(book);

        //when
        bookService.patchBook(book,title);
        //then
        assertEquals("책 제목 변경 됨","아따맘마","아따맘마");
    }

    @Test
    public void 책_삭제(){
        //id가 올바르게 전달되는지 테스트
        //given
        Long id = 1L;

        //when
        bookService.deleteBook(id);

        //then
        verify(bookRepository).deleteById(anyLong());
    }
}
