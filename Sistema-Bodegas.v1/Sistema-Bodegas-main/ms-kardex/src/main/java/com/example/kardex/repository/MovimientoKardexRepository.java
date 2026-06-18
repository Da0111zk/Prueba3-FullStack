package com.example.kardex.repository;

import com.example.kardex.model.MovimientoKardex;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovimientoKardexRepository extends JpaRepository<MovimientoKardex, Long> {
    List<MovimientoKardex> findByProductoId(Long productoId);
    List<MovimientoKardex> findByBodegaId(Long bodegaId);
    List<MovimientoKardex> findByTipoMovimiento(String tipoMovimiento);
    List<MovimientoKardex> findByProductoIdAndBodegaId(Long productoId, Long bodegaId);
}