package com.catpawdogpaw.theartimposter.security.api.controller.user;

import com.catpawdogpaw.theartimposter.security.api.entity.UserEntity;
import com.catpawdogpaw.theartimposter.security.api.service.UserService;
import com.catpawdogpaw.theartimposter.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<?> getUserInfo(@RequestHeader("Authorization") String token) {
        if (token == null || !token.startsWith("Bearer ")) {

            return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
        }

        String accessToken = token.substring(7);

        if (jwtUtil.isExpired(accessToken)) {

            return new ResponseEntity<>("Invalid access token", HttpStatus.UNAUTHORIZED);
        }

        String id = jwtUtil.getId(accessToken);
        UserEntity user = userService.findById(id);

        if (user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}

