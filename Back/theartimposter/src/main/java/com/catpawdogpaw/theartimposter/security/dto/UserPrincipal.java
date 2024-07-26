package com.catpawdogpaw.theartimposter.security.dto;

import com.catpawdogpaw.theartimposter.security.api.entity.RoleType;
import com.catpawdogpaw.theartimposter.security.api.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class UserPrincipal implements OAuth2User {

    private final UserEntity userEntity;
    private Map<String, Object> attributes;

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new SimpleGrantedAuthority(RoleType.USER.getCode())); // 권한 부여
        return collection;
    }

    @Override
    public String getName() {
        return userEntity.getNickname();
    }
    public String getId() {
        return userEntity.getId();
    }
}
