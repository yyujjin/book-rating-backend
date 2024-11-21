package com.example.bookrating.service;

import com.example.bookrating.dto.CustomOAuth2User;
import com.example.bookrating.dto.GoogleResponseDTO;
import com.example.bookrating.dto.UserDetailsDTO;
import com.example.bookrating.entity.UserEntity;
import com.example.bookrating.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{
        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println("oAuth2User : "+oAuth2User);

        GoogleResponseDTO googleResponseDTO = new GoogleResponseDTO(oAuth2User.getAttributes());
        UserEntity existData = userRepository.findByProviderId(googleResponseDTO.getProviderId());

        if (existData==null){
            UserEntity userEntity = new UserEntity();
            userEntity.setProviderId(googleResponseDTO.getProviderId());
            userEntity.setRole("ROLE_USER");
            userRepository.save(userEntity);
        }

        UserDetailsDTO userDetailsDTO = new UserDetailsDTO();
        userDetailsDTO.setProviderId(googleResponseDTO.getProviderId());
        userDetailsDTO.setUsername(googleResponseDTO.getEmail().split("@")[0]);
        userDetailsDTO.setEmail(googleResponseDTO.getEmail());
        userDetailsDTO.setAvatar(googleResponseDTO.getPicture());
        userDetailsDTO.setRole("ROLE_USER");

        return new CustomOAuth2User(userDetailsDTO);
    }
}
