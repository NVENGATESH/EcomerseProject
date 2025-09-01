package com.ecommerce.project.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class CorsConfig implements WebMvcConfigurer{
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins(
             "https://eco-store-git-main-nvengateshs-projects.vercel.app/",
                " eco-store-five.vercel.app",
                "http://localhost:5173"
            )
            .allowedMethods("*")
            .allowCredentials(true);
    }
}
