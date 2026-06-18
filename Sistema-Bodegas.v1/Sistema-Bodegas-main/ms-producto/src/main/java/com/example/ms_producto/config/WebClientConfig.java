package com.example.ms_producto.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${ms.categorias.url}")
    private String categoriasUrl;

    @Bean(name = "webClientCategorias")
    public WebClient webClientCategorias(WebClient.Builder builder) {
        return builder.baseUrl(categoriasUrl).build();
    }
}