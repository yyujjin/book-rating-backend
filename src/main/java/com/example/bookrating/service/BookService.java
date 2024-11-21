package com.example.bookrating.service;

import com.example.bookrating.dto.RequestBookDTO;
import com.example.bookrating.dto.ResponseBookDTO;
import com.example.bookrating.dto.TagDTO;
import com.example.bookrating.entity.Book;
import com.example.bookrating.entity.Review;
import com.example.bookrating.entity.Tag;
import com.example.bookrating.repository.BookRepository;
import com.example.bookrating.repository.ReviewRepository;
import com.example.bookrating.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class BookService {

    private final BookRepository bookRepository;
    private final TagRepository tagRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewService reviewService;

    public List<Book> getBooks () {
        return bookRepository.findAll();
    }

    //100개씩 페이징 처리
    public List<ResponseBookDTO> getBooksByPaging(int page) {
        Pageable pageable = PageRequest.of(page-1, 100); // 한 페이지에 10개의 아이템
        List<Book> bookList =  bookRepository.findAll(pageable).getContent();
        List<ResponseBookDTO> bookDTO = new ArrayList<>();
        for(Book book : bookList) {
            ResponseBookDTO dto = new ResponseBookDTO();
            dto.setId(book.getId());
            dto.setIsbn(book.getIsbn());
            dto.setTitle(book.getTitle());
            dto.setThumbnail(book.getBookCoverUrl());
            dto.setAverageRating(reviewService.calculateAverage(reviewService.getRatings(book.getId())));

            dto.setTags(
                    book.getTags()
                            .stream()
                            .map(tag -> {
                                TagDTO tagDTO = new TagDTO();
                                tagDTO.setId(tag.getId());
                                tagDTO.setName(tag.getName());
                                return tagDTO;
                            })
                            .collect(Collectors.toList()));

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
    public void createBook(RequestBookDTO bookDTO) {
        Book book = new Book();
        book.setIsbn(bookDTO.getIsbn());
        book.setTitle(bookDTO.getTitle());
        book.setBookCoverUrl(bookDTO.getThumbnail());

        Set<Tag> tags = bookDTO.getTags()
                .stream()
                .map(tagId -> tagRepository.findById(tagId)
                        .orElseThrow(() -> new IllegalArgumentException("태그를 찾을 수 없습니다. ID: " + tagId)))
                .collect(Collectors.toSet());
        book.setTags(tags);
        bookRepository.save(book);
        log.info("책 저장 완료!");
    }

    //id로 책 찾기
    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    //isbn으로 책 찾기
    public ResponseBookDTO getBookByIsbn(String isbn) {
        Optional<Book> book = bookRepository.findByIsbn(isbn);
        ResponseBookDTO bookDTO = new ResponseBookDTO();
        bookDTO.setId(book.get().getId());
        bookDTO.setIsbn(book.get().getIsbn());
        bookDTO.setTitle(book.get().getTitle());
        bookDTO.setAverageRating(book.get().getAverage());
        bookDTO.setThumbnail(book.get().getBookCoverUrl());
        bookDTO.setTags(
                book.get().getTags()
                        .stream()
                        .map(tag -> {
                            TagDTO tagDTO = new TagDTO();
                            tagDTO.setId(tag.getId());
                            tagDTO.setName(tag.getName());
                            return tagDTO;
                        })
                        .collect(Collectors.toList()));
        return bookDTO;
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

    //리뷰 존재 확인하기 -> 리뷰 존재시 true 반환
    public boolean findReview(Long id) {
        Optional<List<Review>> review = reviewRepository.findByBookId(id);
        if (review.get().isEmpty()) return false;
        return true;
    }
}

