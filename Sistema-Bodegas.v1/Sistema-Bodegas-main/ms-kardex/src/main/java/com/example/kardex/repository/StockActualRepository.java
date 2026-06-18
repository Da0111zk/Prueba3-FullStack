package com.example.kardex.repository;

import com.example.kardex.model.StockActual;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockActualRepository extends JpaRepository<StockActual, Long> {
    Optional<StockActual> findByProductoIdAndBodegaId(Long productoId, Long bodegaId);
}