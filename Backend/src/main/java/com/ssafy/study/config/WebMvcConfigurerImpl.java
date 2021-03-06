package com.ssafy.study.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfigurerImpl implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
        		.allowedOrigins("*")
//                .allowedOrigins("http://localhost:8080")
        		.allowedMethods("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH")
        		.exposedHeaders("accessToken", "refreshToken");
//                .exposedHeaders("Authorization"); 
    }
}