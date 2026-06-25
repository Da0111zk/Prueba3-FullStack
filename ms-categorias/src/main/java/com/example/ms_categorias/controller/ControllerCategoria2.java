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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/categorias2")
@RequiredArgsConstructor
@Tag(name = "Categorías V2", description = "Controlador de prueba para la gestión de categorías con Swagger")
public class ControllerCategoria2 {

    private final ServicioCategoria servicioCategoria;

    @GetMapping
    @Operation(summary = "Obtener categorías", description = "Lista todas las categorías registradas")
    @ApiResponse(responseCode = "200", description = "Lista obtenida con éxito")
    public ResponseEntity<List<DtoResponseCategoria>> obtenerCategorias() {
        return ResponseEntity.ok(servicioCategoria.obtenerCategorias());
    }

    @GetMapping("/{categoriaId}")
    @Operation(summary = "Obtener categoría por ID", description = "Busca una categoría específica usando su identificador único")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categoría encontrada", 
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DtoResponseCategoria.class))),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    public ResponseEntity<DtoResponseCategoria> obtenerPorId(@PathVariable Long categoriaId) {
        return servicioCategoria.obtenerPorId(categoriaId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear categoría", description = "Registra una nueva categoría en el sistema")
    @ApiResponse(responseCode = "201", description = "Categoría creada exitosamente",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = DtoResponseCategoria.class)))
    public ResponseEntity<DtoResponseCategoria> crear(@Valid @RequestBody DtoCategoria dtoCategoria) {
        DtoResponseCategoria nueva = servicioCategoria.guardar(dtoCategoria);
        return ResponseEntity.status(201).body(nueva);
    }

    @PutMapping("/{categoriaId}")
    @Operation(summary = "Actualizar una categoría", description = "Actualiza los datos de una categoría existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categoría actualizada exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DtoResponseCategoria.class))),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    }) // Aquí se cerraban las anotaciones de Swagger que faltaban
    public ResponseEntity<DtoResponseCategoria> actualizar(@PathVariable Long categoriaId, @Valid @RequestBody DtoCategoria dtoCategoria) {
        return servicioCategoria.obtenerPorId(categoriaId)
                .map(existente -> {
                    // Nota: Si en tu DtoCategoria el setter se llama "setId", cambia esta línea
                    dtoCategoria.setCategoriaId(categoriaId);
                    return ResponseEntity.ok(servicioCategoria.guardar(dtoCategoria));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{categoriaId}")
    @Operation(summary = "Eliminar una categoría", description = "Elimina una categoría del sistema por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Categoría eliminada exitosamente"),
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