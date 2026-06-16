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

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    @GetMapping
    public ResponseEntity<List<Producto>> obtenerTodos() {
        return ResponseEntity.ok(productoService.obtenerTodos());
    }

    @GetMapping("/{productoId}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Long productoId) {
        return productoService.obtenerPorId(productoId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Producto> crear(@Valid @RequestBody Producto producto) {
        Producto nuevo = productoService.guardar(producto);
        return ResponseEntity.status(201).body(nuevo);
    }

    @PutMapping("/{productoId}")
    public ResponseEntity<Producto> actualizar(@PathVariable Long productoId, @Valid @RequestBody Producto datos) {
        return productoService.obtenerPorId(productoId)
                .map(existente -> {
                    datos.setProductoId(productoId);
                    return ResponseEntity.ok(productoService.guardar(datos));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{productoId}")
    public ResponseEntity<Void> eliminar(@PathVariable Long productoId) {
        if (productoService.obtenerPorId(productoId).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        productoService.eliminar(productoId);
        return ResponseEntity.noContent().build();
    }
}