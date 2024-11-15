package com.example.bookrating.service;

import com.example.bookrating.dto.BookDTO;
import com.example.bookrating.dto.TagDTO;
import com.example.bookrating.entity.Book;
import com.example.bookrating.entity.Tag;
import com.example.bookrating.repository.BookRepository;
import com.example.bookrating.repository.TagRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BookService {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    TagRepository tagRepository;

    public List<Book> getBooks () {

        return bookRepository.findAll();
    }

    //10개씩 페이징 처리
    public List<BookDTO> getBooksByPaging(int page) {
        Pageable pageable = PageRequest.of(page-1, 10); // 한 페이지에 10개의 아이템
        List<Book> bookList =  bookRepository.findAll(pageable).getContent();
        List<BookDTO> bookDTO = new ArrayList<>();
        for(Book book : bookList) {
            BookDTO dto = new BookDTO();
            dto.setId(book.getId());
            dto.setIsbn(book.getIsbn());
            dto.setTitle(book.getTitle());
            dto.setBookCoverUrl(book.getBookCoverUrl());

            List<TagDTO> tagDTOs = book.getTags().stream()
                    .map(tag -> new TagDTO(tag.getId(), tag.getName()))
                    .collect(Collectors.toList());
            dto.setTags(tagDTOs);

            bookDTO.add(dto);
        }

        return bookDTO;
    }

    //책 중복 확인
    public boolean isDuplicateBook(String isbn) {
        if (bookRepository.findByIsbn(isbn).isPresent()){
            log.info("중복 Isbn");
            return true;
        }
        return false;
    }

    //책 저장
    public void createBook(BookDTO bookDTO) {
        Book book = new Book();
        book.setIsbn(bookDTO.getIsbn());
        book.setTitle(bookDTO.getTitle());
        book.setBookCoverUrl(bookDTO.getBookCoverUrl());

        Set<Tag> tags = new HashSet<>();

        if (!bookDTO.getIsbn().isEmpty()) {
            for (int i=0; i<bookDTO.getTagIds().size(); i++) {
               tags.add(tagRepository.findById(bookDTO.getTagIds().get(i)).get());
            }
        }
        book.setTags(tags);
        bookRepository.save(book);
        log.info("책 저장 완료!");
    }

    //id로 책 찾기
    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    //isbn으로 책 찾기
    public Optional<Book> getBookByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }

    //책 정보 수정
    public Optional<Book> editBook(Book book,String title){
        book.setTitle(title);
        return Optional.of(bookRepository.save(book));
    }

    //책 삭제
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

}

