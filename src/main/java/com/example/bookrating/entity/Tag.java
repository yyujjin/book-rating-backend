package com.example.bookrating.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//어노테이션 => Tag라는 테이블과 연결된다는 뜻
//엔티티 어노테이션을 붙임으로써 클래스 이름을 가진 테이블 자동으로 생성됨.
//엔티티는 무조건 기본키가 되는 컬럼을 알려줘야한다.
@Entity
@Getter
@Setter
@AllArgsConstructor //생성자 어노테이션
@NoArgsConstructor //기본 생성자 어노테이션
public class Tag {
    @Id //이 어노테이션 자체가 기본키라는 거임
//    @GeneratedValue //자동 증가
    @GeneratedValue(strategy = GenerationType.IDENTITY) //id 자동생성 시키는 방식이 여러개가 있는데 일단 보류
    private Long id; //id가 원래는 null이었다가 추가되면 id가 만들어지기에 final은 붙이지 않는다.
    private String name;




}
