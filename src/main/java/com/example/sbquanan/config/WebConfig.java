package com.example.sbquanan.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/user").setViewName("forward:/index.html");
        registry.addViewController("/user/").setViewName("forward:/index.html");
        registry.addViewController("/user/index.html").setViewName("forward:/index.html");
    }
}
