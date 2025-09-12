package com.ecommerce.project;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.repositories.CategoryRepository;
import org.apache.catalina.filters.CorsFilter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfigs implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "https://eco-store-git-main-nvengateshs-projects.vercel.app",
                        "https://eco-store-five.vercel.app",
                        "http://localhost:5173"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")

                .allowCredentials(true);
    }
    @Bean
    public CommandLineRunner initCategories(CategoryRepository categoryRepository) {
        return args -> {
            if (categoryRepository.count() == 0) { // only insert if empty
                categoryRepository.saveAll(List.of(
                        new Category(1L, "Laptop", null),
                        new Category(2L, "Headphone", null),
                        new Category(3L, "Mobile", null),
                        new Category(4L, "Electronics", null),
                        new Category(5l, "Toys", null),
                        new Category(6L, "Fashion", null)
                ));
            }
        };
    }

}
