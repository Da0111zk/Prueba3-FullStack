package com.example.egresos.controller;

import com.example.egresos.dto.EgresoRequestDTO;
import com.example.egresos.dto.EgresoResponseDTO;
import com.example.egresos.service.EgresoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/egresos")
public class EgresoController {

    @Autowired
    private EgresoService service;

    @PostMapping
    public ResponseEntity<EgresoResponseDTO> crear(@Valid @RequestBody EgresoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto));
    }

    @GetMapping
    public ResponseEntity<List<EgresoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EgresoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/pendientes")
    public ResponseEntity<List<EgresoResponseDTO>> listarPendientes() {
        return ResponseEntity.ok(service.listarPendientes());
    }

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<EgresoResponseDTO>> listarPorProducto(@PathVariable Long productoId) {
        return ResponseEntity.ok(service.listarPorProducto(productoId));
    }

    @PutMapping("/{id}/confirmar")
    public ResponseEntity<EgresoResponseDTO> confirmar(@PathVariable Long id) {
        return ResponseEntity.ok(service.confirmar(id));
    }

    @PutMapping("/{id}/anular")
    public ResponseEntity<EgresoResponseDTO> anular(@PathVariable Long id) {
        return ResponseEntity.ok(service.anular(id));
    }
}