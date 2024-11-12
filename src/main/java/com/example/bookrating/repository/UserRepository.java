package com.example.bookrating.repository;

import com.example.bookrating.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity,Long> {

    UserEntity findByProviderId(String providerId);
}
