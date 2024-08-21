package com.example.bookrating.repository;

import com.example.bookrating.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
//<엔티티이름, 프라이머리 키 타입>
public interface TagRepository extends JpaRepository<Tag,Long> {

}
