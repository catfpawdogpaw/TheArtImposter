package com.catpawdogpaw.theartimposter.security.api.repository;

import com.catpawdogpaw.theartimposter.security.api.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    UserEntity findById(String id);
}
