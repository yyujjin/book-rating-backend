package com.example.bookrating.service;

import com.example.bookrating.dto.CustomOAuth2User;
import com.example.bookrating.dto.GoogleResponseDTO;
import com.example.bookrating.dto.UserDTO;
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
            userEntity.setEmail(googleResponseDTO.getEmail());
            userEntity.setAvatar(googleResponseDTO.getPicture());

            userRepository.save(userEntity);

        }else {
            existData.setEmail(googleResponseDTO.getEmail());
            //existData 객체가 기존 데이터의 상태로 불러와져 일부 필드를 업데이트한 후, 변경 사항을 반영하여 저장함
            userRepository.save(existData);
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setProviderId(googleResponseDTO.getProviderId());
        userDTO.setEmail(googleResponseDTO.getEmail());
        userDTO.setAvatar(googleResponseDTO.getPicture());
        log.info(" userDTO.setProviderId : "+googleResponseDTO.getProviderId());
        log.info(("userDTO.setEmail : "+googleResponseDTO.getEmail()));
        log.info("userDTO.setAvatar : "+googleResponseDTO.getPicture());
        return new CustomOAuth2User(userDTO);
    }
}
