package com.catpawdogpaw.theartimposter.security.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.catpawdogpaw.theartimposter.security.api.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findById(String id);

	Optional<UserEntity> findByUserId(Long id);
	


}
