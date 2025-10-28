package com.secureon;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class for Cross-Origin Resource Sharing (CORS) in the GreenGrow backend application.
 * @author GrowGenius
 * @version 1.0 19/11/2023
 */
@Configuration
@EnableWebMvc
public class CorsConfig implements WebMvcConfigurer {

    /**
     * Configures Cross-Origin Resource Sharing (CORS) for the application.
     *
     * @param registry The CorsRegistry to configure CORS for the application.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")           // Permite cualquier origen
                .allowedMethods("*")           // Permite cualquier método (GET, POST, PUT, DELETE, OPTIONS, etc)
                .allowedHeaders("*")           // Permite cualquier header
                .allowCredentials(false)       // No permite credenciales cuando origins="*"
                .maxAge(3600);
    }
}