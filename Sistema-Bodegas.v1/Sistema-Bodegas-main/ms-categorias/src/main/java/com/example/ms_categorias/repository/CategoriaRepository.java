package com.example.ms_categorias.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ms_categorias.modelo.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    Optional<Categoria> findByCategoriaId(Long id);
    // Aquí puedes añadir métodos personalizados en el futuro, ej: findByNombre
}