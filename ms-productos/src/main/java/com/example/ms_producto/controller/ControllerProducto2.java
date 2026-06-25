package com.example.ms_producto.controller;

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

import com.example.ms_producto.dto.DtoResponseProducto;
import com.example.ms_producto.dto.ProductoDto;
import com.example.ms_producto.service.ProductoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/Producto2") // Mantiene el prefijo para pruebas de Swagger
@RequiredArgsConstructor
@Tag(name = "Productos V2", description = "Controlador de prueba para la gestión de productos con Swagger")
public class ControllerProducto2 {

    private final ProductoService productoService;

    @GetMapping
    @Operation(summary = "Obtener productos", description = "Lista todos los productos registrados")
    @ApiResponse(responseCode = "200", description = "Lista obtenida con éxito")
    public ResponseEntity<List<DtoResponseProducto>> obtenerProductos() {
        return ResponseEntity.ok(productoService.obtenerProductos());
    }

    @GetMapping("/{productoId}")
    @Operation(summary = "Obtener producto por ID", description = "Busca un producto específico usando su identificador único")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto encontrado", 
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DtoResponseProducto.class))),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    public ResponseEntity<DtoResponseProducto> obtenerPorId(@PathVariable Long productoId) {
        return productoService.obtenerPorId(productoId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear producto", description = "Registra un nuevo producto en el sistema")
    @ApiResponse(responseCode = "201", description = "Producto creado exitosamente",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = DtoResponseProducto.class)))
    public ResponseEntity<DtoResponseProducto> crear(@Valid @RequestBody ProductoDto productoDto) {
         DtoResponseProducto nuevo = productoService.guardar(productoDto);
        return ResponseEntity.status(201).body(nuevo);
    }

    @PutMapping("/{productoId}") // Unificado a "productoId" en español
    @Operation(summary = "Actualizar un producto", description = "Actualiza los datos de un producto existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DtoResponseProducto.class))),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    public ResponseEntity<DtoResponseProducto> actualizar(@PathVariable Long productoId, @Valid @RequestBody ProductoDto productoDto) {
        // CORRECCIÓN: Sincronizado perfectamente con el método .actualizar() de tu ProductoService
        return productoService.actualizar(productoId, productoDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{productoId}") // Unificado a "productoId" en español
    @Operation(summary = "Eliminar un producto", description = "Elimina un producto del sistema por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Producto eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    public ResponseEntity<Void> eliminar(@PathVariable Long productoId) {
        // CORRECCIÓN: Sincronizado para usar la misma variable de la ruta
        if (productoService.obtenerPorId(productoId).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        productoService.eliminar(productoId);
        return ResponseEntity.noContent().build(); 
    }
}