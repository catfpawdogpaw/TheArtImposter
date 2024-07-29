package com.catpawdogpaw.theartimposter.security.api.controller.user;

import com.catpawdogpaw.theartimposter.config.CacheService;
import com.catpawdogpaw.theartimposter.security.api.entity.UserEntity;
import com.catpawdogpaw.theartimposter.security.api.service.UserService;
import com.catpawdogpaw.theartimposter.security.jwt.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final CacheService cacheService;
    private static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";

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

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {

        // Get refresh token from cookies
        String refresh = getRefreshTokenFromCookies(request.getCookies());

        if (refresh != null) {
            // Extract user ID from the refresh token
            String userId = jwtUtil.getId(refresh);
            // Delete the refresh token and user data from Redis
            cacheService.deleteRefreshToken(userId);
            cacheService.deleteUserData(userId);
        }

        // Clear cookies
        clearCookies(response);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private String getRefreshTokenFromCookies(Cookie[] cookies) {
        if (cookies == null) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if (REFRESH_TOKEN_COOKIE_NAME.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    private void clearCookies(HttpServletResponse response) {

        Cookie refreshTokenCookie = new Cookie(REFRESH_TOKEN_COOKIE_NAME, null);
        refreshTokenCookie.setMaxAge(0);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(false); // HTTPS를 사용하지 않는 경우 false로 설정
        response.addCookie(refreshTokenCookie);
    }
}

