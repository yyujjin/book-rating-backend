package com.example.bookrating.dto;

import lombok.Data;

import java.util.List;

@Data
public class BookRequestDTO {
    private String isbn;
    private String title;
    private List<Long> tags;
    private String thumbnail;
    private String contents;
    private String datetime;
    private String authors;
    private String publisher;
}
