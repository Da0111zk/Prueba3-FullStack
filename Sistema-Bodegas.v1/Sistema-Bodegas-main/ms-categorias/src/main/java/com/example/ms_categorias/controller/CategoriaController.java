package com.example.ms_categorias.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.ms_categorias.modelo.Categoria;
import com.example.ms_categorias.service.ServicioCategoria;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Categorías", description = "Gestión de categorías de productos en el sistema de bodega")
@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
public class CategoriaController {
    private final ServicioCategoria servicioCategoria;
    
    @Operation(summary = "Listar todas las categorías", description = "Retorna el listado completo de categorías registradas")
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping
    public ResponseEntity<List<Categoria>> obtenerCategorias() {
        return ResponseEntity.ok(servicioCategoria.obtenerCategorias());
    }
    @Operation(summary = "Obtener categoría por ID")
    @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Categoría encontrada"),
    @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @GetMapping("/{categoriaId}")
    public ResponseEntity<Categoria> obtenerPorId(@PathVariable Long categoriaId) {
        return servicioCategoria.obtenerPorId(categoriaId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build()); 
    }
    
    @Operation(summary = "Crear una nueva categoría")
    @ApiResponses({
    @ApiResponse(responseCode = "201", description = "Categoría creada correctamente"),
    @ApiResponse(responseCode = "400", description = "Datos de la categoría inválidos")
    })
    @PostMapping
    public ResponseEntity<Categoria> crear(@Valid @RequestBody Categoria categoria){
        Categoria nueva = servicioCategoria.guardar(categoria);
        return ResponseEntity.status(201).body(nueva);
    }

    @PutMapping("/{categoriaId}")
    @Operation(summary = "Actualizar una categoría existente")
    @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Categoría actualizada correctamente"),
    @ApiResponse(responseCode = "404", description = "Categoría no encontrada"),
    @ApiResponse(responseCode = "400", description = "Datos de la categoría inválidos")
    })
    public ResponseEntity<Categoria> actualizar(@PathVariable Long categoriaId, @Valid @RequestBody Categoria datos) {
        return servicioCategoria.obtenerPorId(categoriaId)
                .map(existente -> {
                    datos.setCategoriaId(categoriaId);
                    return ResponseEntity.ok(servicioCategoria.guardar(datos));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{categoriaId}")
    @Operation(summary = "Eliminar una categoría")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Categoría eliminada correctamente"),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    public ResponseEntity<Void> eliminar(@PathVariable Long categoriaId) {
        if (servicioCategoria.obtenerPorId(categoriaId).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        servicioCategoria.eliminar(categoriaId);
        return ResponseEntity.noContent().build(); 
    }
}