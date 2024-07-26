package com.catpawdogpaw.theartimposter.security.api.service;

import com.catpawdogpaw.theartimposter.security.api.entity.UserEntity;
import com.catpawdogpaw.theartimposter.security.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserEntity findById(String id) {
        return userRepository.findById(id).orElse(null);
    }
}
