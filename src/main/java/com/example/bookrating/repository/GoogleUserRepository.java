package com.example.bookrating.repository;

import com.example.bookrating.entity.GoogleUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoogleUserRepository extends JpaRepository<GoogleUser, Long> {

}
