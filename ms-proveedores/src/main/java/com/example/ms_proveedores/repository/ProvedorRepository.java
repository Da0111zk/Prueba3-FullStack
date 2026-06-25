package com.example.ms_proveedores.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ms_proveedores.modelo.Proveedor;


public interface ProvedorRepository extends JpaRepository<Proveedor, Long> {
    

    
}