package com.example.ms_categorias.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ms_categorias.Dto.DtoCategoria;
import com.example.ms_categorias.Dto.DtoResponseCategoria;
import com.example.ms_categorias.service.ServicioCategoria;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/categoria")
@RequiredArgsConstructor
public class CategoriaController {
    private final ServicioCategoria servicioCategoria;
    
    @GetMapping
    public ResponseEntity<List<DtoResponseCategoria>> obtenerCategorias() {
        return ResponseEntity.ok(servicioCategoria.obtenerCategorias());
    }

    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<DtoResponseCategoria> obtenerPorId(@PathVariable Long categoriaId) {
        return servicioCategoria.obtenerPorId(categoriaId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build()); 
    }
//@RequestBody paquete en blanco de tipo categoria el cual se rellena con la informacion en el post
    @PostMapping
    public ResponseEntity<DtoResponseCategoria> crear(@Valid @RequestBody DtoCategoria dtoCategoria){
        DtoResponseCategoria nueva = servicioCategoria.guardar(dtoCategoria);
        return ResponseEntity.status(201).body(nueva);
    }

    @PutMapping("/categoria/{categoriaId}")
    public ResponseEntity<DtoResponseCategoria> actualizar(@PathVariable Long categoriaId, @Valid @RequestBody DtoCategoria dtoCategoria) {
        return servicioCategoria.obtenerPorId(categoriaId)
                .map(existente -> {
                    dtoCategoria.setCategoriaId(categoriaId);
                    return ResponseEntity.ok(servicioCategoria.guardar(dtoCategoria ));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/categoria/{categoriaId}")
    public ResponseEntity<Void> eliminar(@PathVariable Long categoriaId) {
        if (servicioCategoria.obtenerPorId(categoriaId).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        servicioCategoria.eliminar(categoriaId);
        return ResponseEntity.noContent().build(); 
    }
}