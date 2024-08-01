package com.catpawdogpaw.theartimposter.security.api.service;

import org.springframework.stereotype.Service;

import com.catpawdogpaw.theartimposter.security.api.entity.UserEntity;
import com.catpawdogpaw.theartimposter.security.api.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserEntity findById(String id) {
        return userRepository.findById(id).orElse(null);
    }
    public UserEntity findById(Long id) {
    	return userRepository.findByUserId(id).orElse(null);
    }
    
    public void update(UserEntity user) {
    	userRepository.save(user);
    }
}
