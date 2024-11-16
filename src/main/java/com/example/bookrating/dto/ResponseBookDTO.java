package com.example.bookrating.dto;

import lombok.Data;
import java.util.List;

@Data
public class ResponseBookDTO {
    private Long id;
    private String isbn;
    private String title;
    private List<TagDTO> tags;
    private double average;
    private String bookCoverUrl;
}
