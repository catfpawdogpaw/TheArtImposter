package com.catpawdogpaw.theartimposter.security.api.controller;

import com.catpawdogpaw.theartimposter.config.CacheService;
import com.catpawdogpaw.theartimposter.security.api.entity.RefreshEntity;
import com.catpawdogpaw.theartimposter.security.api.repository.RefreshRepository;
import com.catpawdogpaw.theartimposter.security.jwt.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@ResponseBody
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReissueController {

    private static final Logger log = LoggerFactory.getLogger(ReissueController.class);
    private final JwtUtil jwtUtil;
    private final RefreshRepository refreshRepository;
    private final CacheService cacheService;

    private static final String RESPONSE_MESSAGE_REFRESH_TOKEN_NULL = "refresh token null";
    private static final String RESPONSE_MESSAGE_REFRESH_TOKEN_EXPIRED = "refresh token expired";
    private static final String RESPONSE_MESSAGE_INVALID_REFRESH_TOKEN = "invalid refresh token";


    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.debug("Reissue token request received");
        log.debug("Received reissue request with method: {}", request.getMethod());
        //get refresh token
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {

            if (cookie.getName().equals("refresh_token")) {

                refresh = cookie.getValue();
            }
        }

        if (refresh == null) {
            log.warn("Refresh token is null");
            //response status code
            return new ResponseEntity<>(RESPONSE_MESSAGE_REFRESH_TOKEN_NULL, HttpStatus.BAD_REQUEST);
        }

        //expired check
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            log.warn("Refresh token expired: {}", e.getMessage());
            //response status code
            return new ResponseEntity<>(RESPONSE_MESSAGE_REFRESH_TOKEN_EXPIRED, HttpStatus.BAD_REQUEST);
        }

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
//        String category = jwtUtil.getCategory(refresh);

        if (!"refresh".equals(jwtUtil.getCategory(refresh))) {
            log.warn("Invalid refresh token category");
            //response status code
            return new ResponseEntity<>(RESPONSE_MESSAGE_INVALID_REFRESH_TOKEN, HttpStatus.BAD_REQUEST);
        }

        String userId = jwtUtil.getId(refresh);
        //레디스에 refresh 토큰이 저장되어 있는지 확인
        String storedRefreshToken = cacheService.getRefreshToken(userId);
        if (storedRefreshToken == null || !storedRefreshToken.equals(refresh)) {
            // 레디스에 토큰값이 저장되어 있지 않다면 로그아웃 처리됨(Vue에서 처리)
            log.warn("Refresh token does not match stored token");
            return new ResponseEntity<>(RESPONSE_MESSAGE_INVALID_REFRESH_TOKEN, HttpStatus.BAD_REQUEST);
        }

        String id = jwtUtil.getId(refresh);
        String username = jwtUtil.getUsername(refresh);
        String role = jwtUtil.getRole(refresh);

        //make new JWT
        String newAccess = jwtUtil.createJwt("access", username, id, role, 600000L);
        String newRefresh = jwtUtil.createJwt("refresh", username, id, role, 86400000L);

        //Refresh 토큰 저장 DB에 기존의 Refresh 토큰 삭제 후 새 Refresh 토큰 저장
//        refreshRepository.deleteByRefresh(refresh);
//        addRefreshEntity(username, newRefresh, 86400000L);
        cacheService.saveRefreshToken(Long.parseLong(userId), newRefresh, 86400000L);
        log.debug("New tokens created and stored for user: {}", userId);

        //response as parameters
        Map<String, String> tokens = new HashMap<>();
        tokens.put("access", newAccess);
        tokens.put("refresh", newRefresh);

        log.debug("Token reissue process completed successfully for user: {}", userId);
        return new ResponseEntity<>(tokens, HttpStatus.OK);
    }

//    private void addRefreshEntity(String username, String refresh, Long expiredMs) {
//
//        Date date = new Date(System.currentTimeMillis() + expiredMs);
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
//
//        RefreshEntity refreshEntity = new RefreshEntity();
//        refreshEntity.setUsername(username);
//        refreshEntity.setRefresh(refresh);
//        refreshEntity.setExpiration(dateFormat.format(date));
//
//        refreshRepository.save(refreshEntity);
//    }

//    private Cookie createCookie(String key, String value) {
//
//        Cookie cookie = new Cookie(key, value);
//        cookie.setMaxAge(24*60*60);
//        //cookie.setSecure(true);
//        //cookie.setPath("/");
//        cookie.setHttpOnly(true);
//
//        return cookie;
//    }
}