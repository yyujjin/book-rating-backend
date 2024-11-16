package com.example.bookrating.dto;

import lombok.Data;
import java.util.List;

@Data
public class BookDTO {
    private Long id;
    private String isbn;
    private String title;
    private List<Long> tagIds;
    private double average;
    private String bookCoverUrl;
}
