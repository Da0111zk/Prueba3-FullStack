package com.example.ms_categorias.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.ms_categorias.modelo.Categoria;
import com.example.ms_categorias.service.ServicioCategoria;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
public class CategoriaController {
    private final ServicioCategoria servicioCategoria;
    
    @GetMapping
    public ResponseEntity<List<Categoria>> obtenerCategorias() {
        return ResponseEntity.ok(servicioCategoria.obtenerCategorias());
    }

    @GetMapping("/{categoriaId}")
    public ResponseEntity<Categoria> obtenerPorId(@PathVariable Long categoriaId) {
        return servicioCategoria.obtenerPorId(categoriaId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build()); 
    }
    
    @PostMapping
    public ResponseEntity<Categoria> crear(@Valid @RequestBody Categoria categoria){
        Categoria nueva = servicioCategoria.guardar(categoria);
        return ResponseEntity.status(201).body(nueva);
    }

    @PutMapping("/{categoriaId}")
    public ResponseEntity<Categoria> actualizar(@PathVariable Long categoriaId, @Valid @RequestBody Categoria datos) {
        return servicioCategoria.obtenerPorId(categoriaId)
                .map(existente -> {
                    datos.setCategoriaId(categoriaId);
                    return ResponseEntity.ok(servicioCategoria.guardar(datos));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{categoriaId}")
    public ResponseEntity<Void> eliminar(@PathVariable Long categoriaId) {
        if (servicioCategoria.obtenerPorId(categoriaId).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        servicioCategoria.eliminar(categoriaId);
        return ResponseEntity.noContent().build(); 
    }
}