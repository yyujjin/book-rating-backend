package com.example.bookrating.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //id가 원래는 null이었다가 추가되면 id가 만들어지기에 final은 붙이지 않는다.
    private String name;

    @ManyToMany(mappedBy = "tags")
    @JsonBackReference
    private Set<Book> books;
}
