package com.example.bookrating.service;

import com.example.bookrating.dto.GoogleResponseDTO;
import com.example.bookrating.dto.GoogleUserDTO;
import com.example.bookrating.entity.GoogleUser;
import com.example.bookrating.repository.GoogleUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final GoogleUser googleUser;
    private final GoogleUserRepository googleUserRepository;

    public CustomOAuth2UserService(GoogleUser googleUser, GoogleUserRepository googleUserRepository) {
        this.googleUser = googleUser;
        this.googleUserRepository = googleUserRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        GoogleResponseDTO googleResponseDTO= new GoogleResponseDTO(oAuth2User.getAttributes());

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        //TODO:업데이트하기

        int index = googleResponseDTO.getEmail().indexOf("@");
        String userId = googleResponseDTO.getEmail().substring(0, index);

        googleUser.setUserId(userId);
        googleUser.setName(googleResponseDTO.getName());
        googleUser.setEmail(googleResponseDTO.getEmail());
        googleUser.setRole("ROLE_USER");
        googleUser.setRegistrationId(googleResponseDTO.getProviderId());
        googleUserRepository.save(googleUser);

        return oAuth2User;
    }
}
