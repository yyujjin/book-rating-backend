package com.example.bookrating.controller;

import com.example.bookrating.dto.UserDTO;
import com.example.bookrating.service.UserService;
import com.example.bookrating.util.TokenExtractor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class UserController {

    private final TokenExtractor tokenExtractor;
    private final UserService userService;
    public UserController(TokenExtractor tokenExtractor, UserService userService) {
        this.tokenExtractor = tokenExtractor;
        this.userService = userService;
    }

    //회원가입
    @PostMapping("/auth/register")
    public ResponseEntity<?> register (@RequestBody UserDTO userDTO) {
        if(userService.findByUserId(userDTO.getUsername())){return  ResponseEntity.badRequest().build();}

       return  ResponseEntity.ok().body(userService.saveUser(userDTO));
    }

    //로그인
    @PostMapping("/auth/login")
    public ResponseEntity<?> login (@ModelAttribute UserDTO userDTO) {
        return  ResponseEntity.ok().body("로그인 완료");
    }


    //유저 정보 반환
    @PostMapping("/loginInfo")
    public ResponseEntity<UserDTO> login(HttpServletRequest request) {

        UserDTO userDTO =  tokenExtractor.getUserInfoFromToken(request);
        if(userDTO==null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        return ResponseEntity.ok(userDTO);

    }

}
