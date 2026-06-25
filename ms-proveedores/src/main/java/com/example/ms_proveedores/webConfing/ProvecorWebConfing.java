package com.example.ms_proveedores.webConfing;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
public class ProvecorWebConfing {
    @Configuration
    public class WebConfig implements WebMvcConfigurer {
        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**")
                    .allowedOrigins("*") // En producción, limita esto a tus dominios
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
        }
    }

}
