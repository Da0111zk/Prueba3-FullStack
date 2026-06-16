package com.example.ajustes.config;

import com.example.ajustes.model.Ajuste;
import com.example.ajustes.repository.AjusteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final AjusteRepository ajusteRepository;

    @Override
    public void run(String... args) {
        if (ajusteRepository.count() > 0) {
            log.info(">>> DataInitializer: la BD ya tiene datos, se omite la carga.");
            return;
        }

        log.info(">>> DataInitializer: Insertando ajustes de prueba...");

        ajusteRepository.save(new Ajuste(
                null,
                1L,
                1L,
                10,
                50,
                60,
                LocalDate.now(),
                "Ingreso por regularización",
                "Admin",
                "PENDIENTE",
                "Carga inicial de prueba"
        ));

        ajusteRepository.save(new Ajuste(
                null,
                2L,
                1L,
                -5,
                30,
                25,
                LocalDate.now(),
                "Ajuste por merma",
                "Supervisor",
                "APROBADO",
                "Registro inicial"
        ));

        ajusteRepository.save(new Ajuste(
                null,
                3L,
                2L,
                15,
                20,
                35,
                LocalDate.now(),
                "Corrección de inventario",
                "Operador",
                "RECHAZADO",
                "Dato de ejemplo"
        ));

        log.info(">>> DataInitializer: Ajustes insertados con éxito.");
    }
}