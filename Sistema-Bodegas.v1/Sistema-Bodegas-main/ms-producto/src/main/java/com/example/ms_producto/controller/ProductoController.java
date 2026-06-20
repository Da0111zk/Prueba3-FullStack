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

import com.example.ms_producto.modelo.Producto;
import com.example.ms_producto.service.ProductoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
@Tag(name = "Productos", description = "Gestión de productos del catálogo de bodega")
public class ProductoController {

    private final ProductoService productoService;
    @Operation(summary = "Listar todos los productos", description = "Retorna el listado completo de productos registrados")
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping
    public ResponseEntity<List<Producto>> obtenerTodos() {
        return ResponseEntity.ok(productoService.obtenerTodos());
    }

    @Operation(summary = "Obtener producto por ID")
    @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Producto encontrado"),
    @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/{productoId}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Long productoId) {
        return productoService.obtenerPorId(productoId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @Operation(summary = "Crear un nuevo producto", description = "Valida la categoría asociada antes de crear el producto")
    @ApiResponses({
    @ApiResponse(responseCode = "201", description = "Producto creado correctamente"),
    @ApiResponse(responseCode = "400", description = "Datos del producto inválidos o categoría no existe")
    })
    @PostMapping
    public ResponseEntity<Producto> crear(@Valid @RequestBody Producto producto) {
        Producto nuevo = productoService.guardar(producto);
        return ResponseEntity.status(201).body(nuevo);
    }


    @Operation(summary = "Actualizar un producto existente")
    @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Producto actualizado correctamente"),
    @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
    @ApiResponse(responseCode = "400", description = "Datos del producto inválidos")
    })
    @PutMapping("/{productoId}")
    public ResponseEntity<Producto> actualizar(@PathVariable Long productoId, @Valid @RequestBody Producto datos) {
        return productoService.obtenerPorId(productoId)
                .map(existente -> {
                    datos.setProductoId(productoId);
                    return ResponseEntity.ok(productoService.guardar(datos));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar un producto")
    @ApiResponses({
    @ApiResponse(responseCode = "204", description = "Producto eliminado correctamente"),
    @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @DeleteMapping("/{productoId}")
    public ResponseEntity<Void> eliminar(@PathVariable Long productoId) {
        if (productoService.obtenerPorId(productoId).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        productoService.eliminar(productoId);
        return ResponseEntity.noContent().build();
    }
}