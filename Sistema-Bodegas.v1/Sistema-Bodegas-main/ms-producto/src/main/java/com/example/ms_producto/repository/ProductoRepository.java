
package com.example.ms_producto.repository;
import com.example.ms_producto.modelo.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

}
// CLASE 1: vacío.
    // En Clase 2 agregaremos: findByTitulo, @Query JPQL, nativeQuery.
