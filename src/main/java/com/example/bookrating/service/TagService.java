package com.example.bookrating.service;

import com.example.bookrating.entity.Tag;
import com.example.bookrating.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TagService {

    private final TagRepository tagRepository;

    public List<Tag> getTag() {
        return tagRepository.findAll(); //전체 데이터 가지고 오기
    }
}
