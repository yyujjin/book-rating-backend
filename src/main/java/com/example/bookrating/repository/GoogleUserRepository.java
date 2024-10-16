package com.example.bookrating.repository;

import com.example.bookrating.entity.GoogleUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GoogleUserRepository extends JpaRepository<GoogleUser, Long> {

    //registrationId 가져오기
    @Query("SELECT  registrationId  FROM GoogleUser g WHERE g.registrationId = :registrationId")
    String getRegistrationId(@Param("registrationId") String registrationId);
}
