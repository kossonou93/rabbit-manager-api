package com.rabbit.manager.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ImageUploadConfig {

    @Value("${upload.path}")
    private String uploadPath;

    @Bean
    public String getUploadPath() {
        return uploadPath;
    }
}

