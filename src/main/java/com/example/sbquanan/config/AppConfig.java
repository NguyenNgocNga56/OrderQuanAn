package com.example.sbquanan.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class AppConfig {

    @Bean
    public ConcurrentHashMap<String, String> tokenStore() {
        return new ConcurrentHashMap<>();
    }
}