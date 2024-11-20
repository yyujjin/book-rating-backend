package com.example.bookrating.controller;

import com.example.bookrating.dto.ReviewDTO;
import com.example.bookrating.dto.UserDTO;
import com.example.bookrating.dto.UserResponseDTO;
import com.example.bookrating.entity.Review;
import com.example.bookrating.entity.UserEntity;
import com.example.bookrating.repository.ReviewRepository;
import com.example.bookrating.repository.UserRepository;
import com.example.bookrating.service.UserService;
import com.example.bookrating.util.TokenExtractor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
public class UserController {

    private final TokenExtractor tokenExtractor;
    private final UserService userService;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    public UserController(TokenExtractor tokenExtractor, UserService userService, UserRepository userRepository, ReviewRepository reviewRepository) {
        this.tokenExtractor = tokenExtractor;
        this.userService = userService;
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
    }

    //회원가입
    @PostMapping("/auth/register")
    public ResponseEntity<?> register (@RequestBody UserDTO userDTO) {
        if(userService.findByUsername(userDTO.getUsername())){return  ResponseEntity.badRequest().build();}

       return  ResponseEntity.ok().body(userService.saveUser(userDTO));
    }

    //로그인
    //문서에는 json 요청이던데 form 로그인으로 하면 안되나?
    @PostMapping("/auth/login")
    public ResponseEntity<?> login (@ModelAttribute UserDTO userDTO) {
        return  ResponseEntity.ok().body("로그인 완료");
    }


    //유저 정보 반환
    @GetMapping("/auth/me")
    public ResponseEntity<UserResponseDTO> login(HttpServletRequest request) {

        UserDTO userDTO =  tokenExtractor.getUserInfoFromToken(request);

        UserResponseDTO userResponseDTO = new UserResponseDTO();
        //유저 아이디 빼내기
        Optional<UserEntity> user = userRepository.findByUsername(userDTO.getUsername());
        userResponseDTO.setId(user.get().getId());
        userResponseDTO.setUsername(userDTO.getUsername());
        List<ReviewDTO> reviewDTOList = new ArrayList<>();

        Optional<List<Review>> review = reviewRepository.getReviewsByUserId(user.get().getId());
        for(Review r : review.get()) {
            ReviewDTO reviewDTO = new ReviewDTO();
            reviewDTO.setId(r.getId());
            reviewDTO.setBookId(r.getBook().getId());
            reviewDTO.setRating(r.getRating());
            reviewDTO.setContent(r.getContent());
            reviewDTO.setUpdatedAt(r.getUpdatedAt());
            reviewDTO.setUserId(r.getUserId());
            //유저 네임은 일단 넣지 않음

            reviewDTOList.add(reviewDTO);
        }
        userResponseDTO.setReviews(reviewDTOList);

        if(userDTO==null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        return ResponseEntity.ok(userResponseDTO);

    }

}
