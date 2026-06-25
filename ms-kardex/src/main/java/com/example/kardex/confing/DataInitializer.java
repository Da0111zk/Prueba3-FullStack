package com.example.kardex.confing;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.kardex.model.MovimientoKardex;
import com.example.kardex.model.StockActual;
import com.example.kardex.repository.MovimientoKardexRepository;
import com.example.kardex.repository.StockActualRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;

@Slf4j
@Component
@RequiredArgsConstructor // Inyecta automáticamente ambos repositorios gracias a 'final'
public class DataInitializer implements CommandLineRunner {

    // 1. Añadimos AMBOS repositorios como constantes
    private final MovimientoKardexRepository movimientoRepository;
    private final StockActualRepository stockRepository;

    @Override
    public void run(String... args) {
        // Validamos si ya hay datos para evitar duplicados en la BD
        if (movimientoRepository.count() > 0 || stockRepository.count() > 0) {
            log.info(">> Datos de Kardex ya existen en Laragon, omitiendo carga.");
            return;
        }

        log.info(">> Iniciando carga congruente de Movimientos y Stock...");
        Faker faker = new Faker(new java.util.Locale("es"));

        // Haremos un bucle de 5 productos de prueba
        for (long i = 1; i <= 5; i++) {
            Long productoId = i;
            Long bodegaId = 1L; // Todos en la bodega 1 
            Integer cantidadInicial = faker.number().numberBetween(50, 100);

            // 2. Guardamos el MODELO de Movimiento (La Auditoría)
            movimientoRepository.save(new MovimientoKardex(
                null, 
                productoId, 
                bodegaId, 
                "INGRESO", 
                cantidadInicial, 
                "POSITIVO", 
                0,                  
                cantidadInicial,    
                LocalDate.now(), 
                "Carga inicial automática"
            ));

            // Stock Actual (
            // Debe tener los mismos IDs y la misma cantidad
            stockRepository.save(new StockActual(
                null, 
                productoId, 
                bodegaId, 
                cantidadInicial 
            ));
        }

        log.info(">> ¡Carga doble finalizada con éxito! Tablas sincronizadas.");
    }
}