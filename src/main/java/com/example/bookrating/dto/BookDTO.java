package com.example.bookrating.dto;

import com.example.bookrating.entity.Tag;
import lombok.Data;
import java.util.List;

@Data
public class BookDTO {
    private String isbn;
    private String title;
    private List<Long> tagIds;
}
