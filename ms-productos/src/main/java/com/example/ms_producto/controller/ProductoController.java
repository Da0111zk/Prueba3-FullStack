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

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    @GetMapping
    public ResponseEntity<List<DtoResponseProducto>> obtenerProductos() {
        return ResponseEntity.ok(productoService.obtenerProductos());
    }

    @GetMapping("/productId")
    public ResponseEntity<DtoResponseProducto> obtenerPorId(@PathVariable Long productId) {
        return productoService.obtenerPorId(productId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build()); 
    }

    @PostMapping
    public ResponseEntity<DtoResponseProducto> crear(@Valid @RequestBody ProductoDto productoDto) {
        DtoResponseProducto nuevo = productoService.guardar(productoDto);
        return ResponseEntity.status(201).body(nuevo);
    }

    @PutMapping("/productId")
    public ResponseEntity<DtoResponseProducto> actualizar(@PathVariable Long productId, @Valid @RequestBody ProductoDto productoDto) {
        // CORRECCIÓN: Ahora llamamos directamente al método .actualizar() de tu servicio
        return productoService.actualizar(productId, productoDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/productId")
    public ResponseEntity<Void> eliminar(@PathVariable Long productId) {
        if (productoService.obtenerPorId(productId).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        productoService.eliminar(productId);
        return ResponseEntity.noContent().build(); 
    }
}