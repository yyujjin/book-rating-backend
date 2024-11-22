package com.example.bookrating.controller;

import com.example.bookrating.entity.Tag;
import com.example.bookrating.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RequiredArgsConstructor
@RestController
@CrossOrigin
@io.swagger.v3.oas.annotations.tags.Tag(name = "Tag", description = "태그 관리 API")
public class TagController {

    private final TagService tagService;

    @GetMapping("/tags")
    @Operation(summary = "태그 목록 조회", description = "전체 태그 목록을 조회합니다.")
    public List<Tag> getTag() {
        return tagService.getTag();
    }

}
