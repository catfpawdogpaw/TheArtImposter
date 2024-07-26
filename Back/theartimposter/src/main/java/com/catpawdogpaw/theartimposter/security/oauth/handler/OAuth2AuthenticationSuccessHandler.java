package com.catpawdogpaw.theartimposter.security.oauth.handler;

import com.catpawdogpaw.theartimposter.security.api.entity.RefreshEntity;
import com.catpawdogpaw.theartimposter.security.api.repository.RefreshRepository;
import com.catpawdogpaw.theartimposter.security.dto.UserPrincipal;
import com.catpawdogpaw.theartimposter.security.jwt.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        //OAuth2User
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        String id = userPrincipal.getId();
        String username = userPrincipal.getName();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> authoritiesIterator = authorities.iterator();
        GrantedAuthority authority = authoritiesIterator.next();
        String role = authority.getAuthority();

        //토큰 생성
        String access = jwtUtil.createJwt("access", username, id, role, 600000L);
        String refresh = jwtUtil.createJwt("refresh", username, id, role, 86400000L);

        //Refresh 토큰 저장
        addRefreshEntity(username, refresh, 86400000L);

        //응답 설정
        // 쿼리 파라미터로 access 및 refresh 토큰 전달
        String targetUrl = UriComponentsBuilder.fromUriString("http://localhost:3000/")
                .queryParam("access", access)
                .queryParam("refresh", refresh)
                .build().toUriString();

        response.sendRedirect(targetUrl);
    }

    private void addRefreshEntity(String username, String refresh, long expiredMs) {
        Date date = new Date(System.currentTimeMillis() + expiredMs);

        RefreshEntity refreshEntity = new RefreshEntity();
        refreshEntity.setUsername(username);
        refreshEntity.setRefresh(refresh);
        refreshEntity.setExpiration(date.toString());

        refreshRepository.save(refreshEntity);
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        //cookie.setSecure(true);
        //cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
}
