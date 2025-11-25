package com.roteiro.roteiro_service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Permite CORS para todos os endpoints da API (/**)
                .allowedOrigins("http://localhost:4200", "http://localhost") // Permite requisições do Front-end em dev e em produção (Docker)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Permite todos os verbos HTTP do CRUD
                .allowedHeaders("*") // Permite todos os headers
                .allowCredentials(true); // Permite cookies e credenciais
    }
}
