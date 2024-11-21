package com.example.bookrating.controller;

import com.example.bookrating.dto.UserProfileReviewDTO;
import com.example.bookrating.dto.UserDetailsDTO;
import com.example.bookrating.dto.UserResponseDTO;
import com.example.bookrating.entity.Review;
import com.example.bookrating.entity.UserEntity;
import com.example.bookrating.repository.ReviewRepository;
import com.example.bookrating.repository.UserRepository;
import com.example.bookrating.service.UserService;
import com.example.bookrating.util.TokenExtractor;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@CrossOrigin
public class UserController {

    private final TokenExtractor tokenExtractor;
    private final UserService userService;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    //회원가입
    @PostMapping("/auth/register")
    public ResponseEntity<?> register (@RequestBody UserDetailsDTO userDetailsDTO) {
        if(userService.findByUsername(userDetailsDTO.getUsername())){return  ResponseEntity.badRequest().build();}

       return  ResponseEntity.ok().body(userService.saveUser(userDetailsDTO));
    }

    //문서에는 json 요청이던데 form 로그인으로 하면 안되나?
    @PostMapping("/auth/login")
    public ResponseEntity<?> login (@ModelAttribute UserDetailsDTO userDetailsDTO) {
        return  ResponseEntity.ok().body("로그인 완료");
    }

    //유저 정보 반환
    @GetMapping("/auth/me")
    public ResponseEntity<UserResponseDTO> login(HttpServletRequest request) {

        UserDetailsDTO userDetailsDTO =  tokenExtractor.getUserInfoFromToken(request);

        UserResponseDTO userResponseDTO = new UserResponseDTO();
        //유저 아이디 빼내기
        Optional<UserEntity> user = userRepository.findByUsername(userDetailsDTO.getUsername());
        userResponseDTO.setId(user.get().getId());
        userResponseDTO.setUsername(userDetailsDTO.getUsername());
        List<UserProfileReviewDTO> userProfileReviewDTOList = new ArrayList<>();

        Optional<List<Review>> review = reviewRepository.getReviewsByUserId(user.get().getId());
        for(Review r : review.get()) {
            UserProfileReviewDTO userProfileReviewDTO = new UserProfileReviewDTO();
            userProfileReviewDTO.setId(r.getId());
            userProfileReviewDTO.setBookId(r.getBook().getId());
            userProfileReviewDTO.setRating(r.getRating());
            userProfileReviewDTO.setContent(r.getContent());
            userProfileReviewDTO.setUpdatedAt(r.getUpdatedAt());
            userProfileReviewDTO.setUserId(r.getUserId());
            userProfileReviewDTO.setUser(r.getUsername());

            userProfileReviewDTOList.add(userProfileReviewDTO);
        }
        userResponseDTO.setReviews(userProfileReviewDTOList);

        if(userDetailsDTO ==null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        return ResponseEntity.ok(userResponseDTO);

    }

}
