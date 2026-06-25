package com.example.ms_categorias.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.ms_categorias.modelo.Categoria;
import com.example.ms_categorias.repository.CategoriaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final CategoriaRepository categoriaRepository;

    @Override
    public void run(String... args) {
    if (categoriaRepository.count() > 0) {
        log.info(">> Categorías ya existen en la BD, omitiendo carga.");
        return;
    }

    log.info(">> Iniciando carga de categorías con DataFaker...");

    Faker faker = new Faker(new java.util.Locale("es"));

    for (int i = 0; i < 10; i++) {
        categoriaRepository.save(new Categoria(
            null,
            faker.commerce().department(),
            faker.lorem().sentence(),
            "ACTIVO"
        ));
    }

    log.info(">> Carga inicial finalizada con éxito.");
}
}

///DATAFAKER