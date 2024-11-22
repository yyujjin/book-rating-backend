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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "User", description = "사용자 관리 API")
public class UserController {

    private final TokenExtractor tokenExtractor;
    private final UserService userService;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    //회원가입
    @PostMapping("/auth/register")
    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다.")
    public ResponseEntity<?> register (@RequestBody UserDetailsDTO userDetailsDTO) {
        if(userService.findByUsername(userDetailsDTO.getUsername())){return  ResponseEntity.badRequest().build();}

       return  ResponseEntity.ok().body(userService.saveUser(userDetailsDTO));
    }

    @PostMapping("/auth/login")
    @Operation(summary = "로그인", description = "폼 기반 로그인을 처리합니다. 사용자 이름과 비밀번호를 제출하여 로그인합니다.")
    public ResponseEntity<?> login (@ModelAttribute UserDetailsDTO userDetailsDTO) {
        return  ResponseEntity.ok().body("로그인 완료");
    }

    //유저 정보 반환
    @GetMapping("/auth/me")
    @Operation(summary = "사용자 정보 조회", description = "현재 로그인한 사용자의 정보를 반환합니다. 포함된 리뷰 목록도 조회됩니다.")
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
