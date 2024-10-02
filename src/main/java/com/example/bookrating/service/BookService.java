package com.example.bookrating.service;

import com.example.bookrating.dto.BookDTO;
import com.example.bookrating.entity.Book;
import com.example.bookrating.entity.Tag;
import com.example.bookrating.repository.BookRepository;
import com.example.bookrating.repository.ReviewRepository;
import com.example.bookrating.repository.TagRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

// TODO 추가, 추가_중복체크, 수정, 삭제 테스트 코드 작성하기
// 작성법 모르겠음 스프링 입문 강의 참고
@Slf4j
@Service
public class BookService {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    TagRepository tagRepository;

    @Autowired
    ReviewRepository reviewRepository;

    public List<Book> getBooks () {
        return bookRepository.findAll();
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

    //책 평균 값 업데이트하기
    public void updateAverage(Book book){
        //bookRepository.save(book);
        bookRepository.updateAverageRating(book.getId(), book.getAverage());
    }
}

