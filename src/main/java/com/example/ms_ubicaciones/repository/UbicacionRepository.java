package com.example.ms_ubicaciones.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ms_ubicaciones.modelo.Ubicacion;

public interface UbicacionRepository extends JpaRepository<Ubicacion, Long> {

}
