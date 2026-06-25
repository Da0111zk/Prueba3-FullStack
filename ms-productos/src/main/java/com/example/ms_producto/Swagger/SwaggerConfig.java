package com.example.ms_producto.Swagger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("2026 informe productos")
                        .version("1.0")
                        .description("APi Documentación de la API para el sistema de gestion de productos"));
    }

}
