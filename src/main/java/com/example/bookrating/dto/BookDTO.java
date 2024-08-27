package com.example.bookrating.dto;

import lombok.Data;
import java.util.List;

@Data
public class BookDTO {
    private String isbn;
    private String title;
    private List tagIds;
}
