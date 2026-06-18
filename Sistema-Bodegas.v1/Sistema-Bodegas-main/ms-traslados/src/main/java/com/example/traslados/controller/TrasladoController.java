package com.example.traslados.controller;

import com.example.traslados.dto.TrasladoRequestDTO;
import com.example.traslados.dto.TrasladoResponseDTO;
import com.example.traslados.service.TrasladoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/traslados")
public class TrasladoController {

    @Autowired
    private TrasladoService service;

    @GetMapping
    public ResponseEntity<List<TrasladoResponseDTO>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrasladoResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<TrasladoResponseDTO>> obtenerPorProducto(@PathVariable Long productoId) {
        return ResponseEntity.ok(service.obtenerPorProducto(productoId));
    }

    @PostMapping
    public ResponseEntity<TrasladoResponseDTO> crear(@Valid @RequestBody TrasladoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrasladoResponseDTO> actualizar(@PathVariable Long id,
                                                          @Valid @RequestBody TrasladoRequestDTO dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }
}