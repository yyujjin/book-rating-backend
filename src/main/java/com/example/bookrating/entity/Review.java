package com.example.bookrating.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private int rating;
    private LocalDateTime updatedAt;
    private Long userId; //사용자 고유 아이디
    private String userAvatar; //사용자 프로필 사진
    private String username;

    @ManyToOne
    @JoinColumn(name = "book_id")  // 외래키 이름을 'bookId'로 설정
    private Book book;

    @PrePersist
    public void prePersist() {
        this.updatedAt = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime();
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", rating=" + rating +
                ", updatedAt=" + updatedAt +
                ", book=" + book +
                '}';
    }
}
