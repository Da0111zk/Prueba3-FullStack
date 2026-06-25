package com.example.kardex.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.kardex.model.StockActual;


public interface StockActualRepository extends JpaRepository<StockActual, Long> {
    Optional<StockActual> findByProductoIdAndBodegaId(Long productoId, Long bodegaId);
}