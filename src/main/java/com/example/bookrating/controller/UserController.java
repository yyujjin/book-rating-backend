package com.example.bookrating.controller;

import com.example.bookrating.dto.UserDTO;
import com.example.bookrating.util.TokenExtractor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class UserController {

    private final TokenExtractor tokenExtractor;

    public UserController(TokenExtractor tokenExtractor) {
        this.tokenExtractor = tokenExtractor;
    }

    @PostMapping("/loginInfo")
    public ResponseEntity<UserDTO> login(HttpServletRequest request) {

        UserDTO userDTO =  tokenExtractor.getUserInfoFromToken(request);
        if(userDTO==null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        return ResponseEntity.ok(userDTO);

    }

}
