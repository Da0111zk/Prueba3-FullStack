package com.example.ubicaciones.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.ubicaciones.modelo.Ubicacion;
import com.example.ubicaciones.repository.UbicacionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UbicacionRepository ubicacionRepository;

    @Override
    public void run(String... args) {
        if (ubicacionRepository.count() > 0) {
            log.info(">> Ubicaciones ya existen en la BD, omitiendo carga.");
            return;
        }

        log.info(">> Iniciando carga de ubicaciones de prueba...");

        List<Ubicacion> ubicaciones = List.of(
                new Ubicacion(null, "Electrónica", "2", 987667876, "19876725-2", "ACTIVO", "electronica@bodega.cl"),
                new Ubicacion(null, "Ropa", "1", 123456789, "12345678-1", "ACTIVO", "ropa@bodega.cl"),
                new Ubicacion(null, "Hogar", "3", 987654321, "98765432-3", "ACTIVO", "hogar@bodega.cl")
        );

        ubicacionRepository.saveAll(ubicaciones);

        log.info(">> Carga inicial finalizada con éxito.");
    }
}