package com.example.ms_producto.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.example.ms_producto.modelo.Producto;
import com.example.ms_producto.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.math.BigDecimal;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final ProductoRepository productoRepository;

    @Override
    public void run(String... args) {
        if (productoRepository.count() > 0) {
            log.info(">>> DataInitializer: la BD ya tiene datos, se omite la carga.");
            return;
        }

        log.info(">>> DataInitializer: Insertando productos de prueba...");

        // Usamos el constructor con Long al final para categoriaId
        productoRepository.save(new Producto(null, "Laptop Gamer", "SKU001", new BigDecimal("1200.00"), "ACTIVO", 1L));
        productoRepository.save(new Producto(null, "Mouse Inalámbrico", "SKU002", new BigDecimal("25.50"), "ACTIVO", 1L));
        productoRepository.save(new Producto(null, "Polera Algodón", "SKU003", new BigDecimal("15.00"), "ACTIVO", 2L));

        log.info(">>> DataInitializer: Productos insertados con éxito.");
    }
}