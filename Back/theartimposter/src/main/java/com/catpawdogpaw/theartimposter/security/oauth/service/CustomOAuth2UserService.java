package com.catpawdogpaw.theartimposter.security.oauth.service;

import com.catpawdogpaw.theartimposter.security.api.entity.ProviderType;
import com.catpawdogpaw.theartimposter.security.api.entity.RoleType;
import com.catpawdogpaw.theartimposter.security.api.entity.UserEntity;
import com.catpawdogpaw.theartimposter.security.dto.UserPrincipal;
import com.catpawdogpaw.theartimposter.security.dto.info.OAuth2UserInfo;
import com.catpawdogpaw.theartimposter.security.dto.info.OAuth2UserInfoFactory;
import com.catpawdogpaw.theartimposter.security.oauth.exception.OAuthProviderMissMatchException;
import com.catpawdogpaw.theartimposter.security.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        try{
            return this.process(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException e) {
            throw e;
        } catch (Exception ex){
            ex.printStackTrace();
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }


    private OAuth2User process(OAuth2UserRequest userRequest, OAuth2User user) {
        ProviderType providerType = ProviderType.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase());
        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(providerType, user.getAttributes());
        UserEntity savedUserEntity = userRepository.findById(userInfo.getId()).orElse(null);

        if (savedUserEntity != null) {
            if (providerType != savedUserEntity.getSocialProviderType()) {
                throw new OAuthProviderMissMatchException(
                        "Looks like you're signed up with " + providerType +
                                " account. Please use your " + savedUserEntity.getSocialProviderType() + " account to login."
                );
            }
            savedUserEntity = updateUserEntity(savedUserEntity, userInfo);
        } else {
            savedUserEntity = creatUserEntity(userInfo, providerType);
        }

        return new UserPrincipal(savedUserEntity, user.getAttributes());
    }

    private UserEntity creatUserEntity(OAuth2UserInfo userInfo, ProviderType providerType) {
        LocalDateTime now = LocalDateTime.now();
        UserEntity userEntity = new UserEntity(
                userInfo.getId(),
                userInfo.getName(),
                userInfo.getEmail(),
                providerType,
                userInfo.getImageUrl(),
                RoleType.USER,
                now,
                now
        );

        return userRepository.saveAndFlush(userEntity);
    }

    private UserEntity updateUserEntity(UserEntity userEntity, OAuth2UserInfo userInfo) {
        if(userEntity.getNickname() != null && !userEntity.getNickname().equals(userInfo.getName())) {
            userEntity.setNickname(userInfo.getName());
        }
        if(userEntity.getProfileImageUrl() != null && !userEntity.getProfileImageUrl().equals(userInfo.getImageUrl())) {
            userEntity.setProfileImageUrl(userInfo.getImageUrl());
        }
        return userRepository.save(userEntity);
    }
}
