package com.example.ms_proveedores.confing;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.ms_proveedores.modelo.Proveedor;
import com.example.ms_proveedores.repository.ProvedorRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final ProvedorRepository provedorRepository;
     @Override
    public void run(String... args) {
        if (provedorRepository.count() > 0) {
            log.info(">>> DataInitializer: la BD ya tiene datos, se omite la carga.");
            return;
        }

        log.info(">>> DataInitializer: Insertando provedores de prueba...");

        // Usamos el constructor con Long al final para categoriaId
        provedorRepository.save(new Proveedor(null, "Importadora del Sur S.A", 187648372, "Ang@gmail.com",98768763,"Disponible"));
        provedorRepository.save(new Proveedor(null, "Servicios y Asesorías del Sur SpA", 187648373, "Proveedor2@gmail.com",98768764,"Disponible"));
        provedorRepository.save(new Proveedor(null, "Empresa Individual de Responsabilidad Limitada", 187648374, "Proveedor3@gmail.com",98768765,"Disponible"));

        log.info(">>> DataInitializer: Provedores insertados con éxito.");
    }


}