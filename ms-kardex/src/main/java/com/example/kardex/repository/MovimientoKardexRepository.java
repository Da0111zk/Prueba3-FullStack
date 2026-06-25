package com.example.kardex.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.kardex.model.MovimientoKardex;


public interface MovimientoKardexRepository extends JpaRepository<MovimientoKardex, Long> {
    List<MovimientoKardex> findByProductoId(Long productoId);
    List<MovimientoKardex> findByBodegaId(Long bodegaId);
    List<MovimientoKardex> findByTipoMovimiento(String tipoMovimiento);
    List<MovimientoKardex> findByProductoIdAndBodegaId(Long productoId, Long bodegaId);
}