
package com.example.ms_producto.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ms_producto.modelo.Productos;

public interface ProductoRepository extends JpaRepository<Productos, Long> {

}
// CLASE 1: vacío.
    // En Clase 2 agregaremos: findByTitulo, @Query JPQL, nativeQuery.
