package com.example.ms_ubicaciones.confing;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.ms_ubicaciones.modelo.Ubicacion;
import com.example.ms_ubicaciones.repository.UbicacionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final UbicacionRepository ubicacionRepository;

    @Override
    public void run(String... args) {
    if (ubicacionRepository.count() > 0) {
        log.info(">> Categorías ya existen en la BD, omitiendo carga.");
        return;
    }

    log.info(">> Iniciando carga de categorías con DataFaker...");

    Faker faker = new Faker(new java.util.Locale("es"));

    for (int i = 0; i < 10; i++) {
    ubicacionRepository.save(new Ubicacion(
        null,                                      
        faker.company().name(),                    
        "Pasillo " + faker.number().numberBetween(1, 10), 
        faker.number().numberBetween(900000000, 999999999), 
        faker.number().numberBetween(10000000, 25000000),    
        "ACTIVO",                                  
        faker.internet().emailAddress()            
    ));
}

     log.info(">> Carga inicial finalizada con éxito.");
}
}

