package com.example.kardex.controller;

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

import com.example.kardex.dto.MovimientoRequestDTO;
import com.example.kardex.dto.MovimientoResponseDTO;
import com.example.kardex.dto.StockResponseDTO;
import com.example.kardex.service.KardexService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/kardex")
@RequiredArgsConstructor
public class KardexController {

    private final KardexService service;
    
    @GetMapping
    public ResponseEntity<List<MovimientoResponseDTO>> obtenerMovimientos() {
        return ResponseEntity.ok(service.obtenerTodosLosMovimientos());
    }

    @GetMapping("/stock/producto/{productoId}/bodega/{bodegaId}")
    public ResponseEntity<StockResponseDTO> obtenerPorId(@PathVariable Long productoId, @PathVariable Long bodegaId) {
        try {
            StockResponseDTO stock = service.obtenerStock(productoId, bodegaId);
            return ResponseEntity.ok(stock);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<MovimientoResponseDTO> crear(@Valid @RequestBody MovimientoRequestDTO dto) {
        MovimientoResponseDTO nueva = service.registrarMovimiento(dto);
        return ResponseEntity.status(201).body(nueva);
    }

    @PutMapping("/stock/producto/{productoId}/bodega/{bodegaId}/cantidad/{nuevaCantidad}")
    public ResponseEntity<StockResponseDTO> actualizar(
            @PathVariable Long productoId, 
            @PathVariable Long bodegaId, 
            @PathVariable Integer nuevaCantidad) {
        try {
            service.obtenerStock(productoId, bodegaId);
            return ResponseEntity.ok(service.actualizarStockManual(productoId, bodegaId, nuevaCantidad));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/stock/producto/{productoId}/bodega/{bodegaId}")
    public ResponseEntity<Void> eliminar(@PathVariable Long productoId, @PathVariable Long bodegaId) {
        try {
            service.obtenerStock(productoId, bodegaId);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
        service.eliminarStock(productoId, bodegaId);
        return ResponseEntity.noContent().build(); 
    }
}