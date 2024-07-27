package com.catpawdogpaw.theartimposter.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 경로에 대해 CORS를 허용합니다.
//                .allowedOrigins("http://localhost:3000") // CORS 요청을 허용할 원본 지정
        		.allowedOrigins("http://localhost:9080")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 허용할 HTTP 메소드
                .allowedHeaders("*") // 모든 헤더의 CORS 요청을 허용
                .allowCredentials(true) // 쿠키를 포함한 요청 허용
                .maxAge(3600); // pre-flight 요청의 결과를 캐시하는 시간(초 단위)
    }
}