package com.example.bookrating.dto;

import lombok.Data;
import java.util.List;

@Data
public class BookResponseDTO {
    private Long id;
    private String isbn;
    private String title;
    private List<TagDTO> tags;
    private double averageRating;
    private String thumbnail;
    private String contents;
    private String datetime;
    private String authors;
    private String publisher;
}

