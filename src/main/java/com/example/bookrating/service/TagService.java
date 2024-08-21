package com.example.bookrating.service;

import com.example.bookrating.entity.Tag;
import com.example.bookrating.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {
    @Autowired
    private TagRepository tagRepository;

    public List<Tag> getTag() {
        return tagRepository.findAll(); //전체 데이터 가지고 오기

    }
}
