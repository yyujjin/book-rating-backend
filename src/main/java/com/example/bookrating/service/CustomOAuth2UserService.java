package com.example.bookrating.service;

import com.example.bookrating.dto.CustomOAuth2User;
import com.example.bookrating.dto.GoogleResponseDTO;
import com.example.bookrating.dto.UserDTO;
import com.example.bookrating.entity.UserEntity;
import com.example.bookrating.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

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

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        if (registrationId.equals("google")){
            GoogleResponseDTO googleResponseDTO = new GoogleResponseDTO(oAuth2User.getAttributes());
            String username = googleResponseDTO.getProvider()+" "+googleResponseDTO.getProviderId();

            UserEntity existData = userRepository.findByUsername(username);

            if (existData==null){
                UserEntity userEntity = new UserEntity();
                userEntity.setUsername(username);
                userEntity.setEmail(googleResponseDTO.getEmail());
                userEntity.setName(googleResponseDTO.getName());
                userEntity.setRole("ROLE_USER");

                userRepository.save(userEntity);

                UserDTO userDTO = new UserDTO();
                userDTO.setUserName(username);
                userDTO.setName(googleResponseDTO.getName());
                userDTO.setRole("ROLE_USER");
                return new CustomOAuth2User(userDTO);

            }else {
                existData.setEmail(googleResponseDTO.getEmail());
                existData.setName(googleResponseDTO.getName());
                userRepository.save(existData);

                UserDTO userDTO = new UserDTO();
                userDTO.setUserName(existData.getUsername());
                userDTO.setName(googleResponseDTO.getName());
                userDTO.setRole(existData.getRole());

                return new CustomOAuth2User(userDTO);
            }

        }
        return null;
    }
}
