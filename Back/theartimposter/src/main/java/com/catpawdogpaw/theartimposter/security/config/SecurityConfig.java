package com.catpawdogpaw.theartimposter.security.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.catpawdogpaw.theartimposter.security.jwt.JwtFilter;
import com.catpawdogpaw.theartimposter.security.jwt.JwtUtil;
import com.catpawdogpaw.theartimposter.security.oauth.handler.OAuth2AuthenticationSuccessHandler;
import com.catpawdogpaw.theartimposter.security.oauth.service.CustomOAuth2UserService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final CustomOAuth2UserService customOAuth2UserService;
	private final JwtUtil jwtUtil;
	private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		// csrf disable
		http.cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {

			@Override
			public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

				CorsConfiguration configuration = new CorsConfiguration();

				configuration.setAllowedOrigins(Collections.singletonList("http://localhost:9080"));
				configuration.setAllowedMethods(Collections.singletonList("*"));
				configuration.setAllowCredentials(true);
				configuration.setAllowedHeaders(Collections.singletonList("*"));
				configuration.setMaxAge(3600L);

				configuration.setExposedHeaders(Collections.singletonList("Set-Cookie"));
				configuration.setExposedHeaders(Collections.singletonList("Authorization"));

				return configuration;
			}
		}));

		// From 로그인 방식 disable
		http.formLogin((auth) -> auth.disable());

		// HTTP Basic 인증 방식 disable
		http.httpBasic((auth) -> auth.disable());

		// JWTFilter 추가
		http.addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

                
        //csrf disable
        http
                .csrf(AbstractHttpConfigurer::disable);
        //From 로그인 방식 disable
        http
                .formLogin(AbstractHttpConfigurer::disable);

        //HTTP Basic 인증 방식 disable
        http
                .httpBasic(AbstractHttpConfigurer::disable);

		// oauth2
		http.oauth2Login((oauth2) -> oauth2
				.userInfoEndpoint(
						(userInfoEndpointConfig) -> userInfoEndpointConfig.userService(customOAuth2UserService))
				.successHandler(oAuth2AuthenticationSuccessHandler));

		// 경로별 인가 작업
		http.authorizeHttpRequests((auth) -> auth.requestMatchers("/","/login","/*", "/api/user/logout").permitAll()
				.requestMatchers("/api/reissue").permitAll()
				.requestMatchers("/api/*").permitAll()
				.requestMatchers("/wait-service/wait-websocket/**", "/waitroom/**").permitAll() // WebSocket
				.requestMatchers("/ws/**").permitAll()
				.anyRequest().authenticated());

		// 세션 설정 : STATELESS
		http.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		return http.build();
	}

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
