package com.example.ms_producto.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.ms_producto.modelo.Productos;
import com.example.ms_producto.repository.ProductoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final ProductoRepository productoRepository;

       @Override
    public void run(String... args) {
    if (productoRepository.count() > 0) {
        log.info(">> Categorías ya existen en la BD, omitiendo carga.");
        return;
    }

    log.info(">> Iniciando carga de categorías con DataFaker...");

Faker faker = new Faker(new java.util.Locale("es"));

for (int i = 0; i < 10; i++) {
    //  Genera  SKU único  @Column(unique = true)
   
    String skuUnico = "PROD-" + faker.code().asin() + i; 

    productoRepository.save(new Productos(
        null,                                        
        faker.commerce().productName(),              
        skuUnico,                                    
        faker.number().numberBetween(5000, 99000),   
        "ACTIVO"                                     
    ));
}

    log.info(">> Carga inicial finalizada con éxito.");
}
}

///DATAFAKER
