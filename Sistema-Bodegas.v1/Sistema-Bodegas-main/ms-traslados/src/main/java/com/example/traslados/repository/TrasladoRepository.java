package com.example.traslados.repository;

import com.example.traslados.model.Traslado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrasladoRepository extends JpaRepository<Traslado, Long> {
    List<Traslado> findByProductoId(Long productoId);
}