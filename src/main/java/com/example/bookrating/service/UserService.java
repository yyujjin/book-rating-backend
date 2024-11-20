package com.example.bookrating.service;

import com.example.bookrating.dto.UserDTO;
import com.example.bookrating.entity.UserEntity;
import com.example.bookrating.repository.UserRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //아이디 중복 확인
    public boolean findByUserId(String userId) {
       Optional<UserEntity> user = userRepository.findByUserId(userId);
       return user.isPresent()?true:false;
    }

    //유저 정보 저장(회원가입 완료)
    public UserEntity saveUser(UserDTO userDTO) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(userDTO.getUserId());
        userEntity.setPassword(userDTO.getPassword());
        userEntity.setRole("ROLE_USER");
        UserEntity savedUser = userRepository.save(userEntity);
        return savedUser;
    }
}

