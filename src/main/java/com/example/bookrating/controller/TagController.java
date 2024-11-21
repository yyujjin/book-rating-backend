package com.example.bookrating.controller;

import com.example.bookrating.entity.Tag;
import com.example.bookrating.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RequiredArgsConstructor
@RestController
@CrossOrigin
public class TagController {

    private final TagService tagService;

    @GetMapping("/tags")
    public List<Tag> getTag() {
        return tagService.getTag();
    }

}
