package com.example.ubicaciones.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ubicaciones.modelo.Ubicacion;

public interface UbicacionRepository extends JpaRepository<Ubicacion, Long> {

}
