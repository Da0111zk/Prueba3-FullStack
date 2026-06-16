package com.example.ajustes.repository;

import com.example.ajustes.model.Ajuste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AjusteRepository extends JpaRepository<Ajuste, Long> {
    List<Ajuste> findByEstado(String estado);
    List<Ajuste> findByProductoId(Long productoId);
    List<Ajuste> findByBodegaId(Long bodegaId);
}