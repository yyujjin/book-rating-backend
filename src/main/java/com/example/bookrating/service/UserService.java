package com.example.bookrating.service;

import com.example.bookrating.dto.UserDetailsDTO;
import com.example.bookrating.entity.UserEntity;
import com.example.bookrating.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //아이디 중복 확인
    public boolean findByUsername(String username) {
       Optional<UserEntity> user = userRepository.findByUsername(username);
       return user.isPresent()?true:false;
    }

    //유저 정보 저장(회원가입 완료)
    public UserEntity saveUser(UserDetailsDTO userDetailsDTO) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(userDetailsDTO.getUsername());

        String encryptedPassword = passwordEncoder.encode(userDetailsDTO.getPassword());
        userEntity.setPassword(encryptedPassword);

        userEntity.setRole("ROLE_USER");
        UserEntity savedUser = userRepository.save(userEntity);
        return savedUser;
    }
}

