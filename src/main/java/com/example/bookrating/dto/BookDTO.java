package com.example.bookrating.dto;

import com.example.bookrating.entity.Tag;
import lombok.Data;
import java.util.List;
import java.util.Set;

@Data
public class BookDTO {
    private Long id;
    private String isbn;
    private String title;
    private List<Long> tagIds;
    private Set<Tag> tags;
    private String bookCoverUrl;
}
