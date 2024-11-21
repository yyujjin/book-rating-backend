package com.example.bookrating.dto;

import lombok.Data;

import java.util.List;

@Data
public class RequestBookDTO {
    private String isbn;
    private String title;
    private List<Long> tags;
    private String thumbnail;
}
