package com.example.ubicaciones.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ubicaciones.modelo.Ubicacion;
import com.example.ubicaciones.service.UbicacionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/ubicaciones")
@RequiredArgsConstructor
public class UbicacionController {
    private final UbicacionService ubicacionService;

    @GetMapping
    public ResponseEntity<Object> obtenerUbicaciones() {
        return ResponseEntity.ok(ubicacionService.obtenerUbicaciones());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ubicacion> obtenerPorId(@PathVariable Long ubicacionId) {
        return ubicacionService.obtenerPorId(ubicacionId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Ubicacion> guardar(@Valid @RequestBody Ubicacion ubicacion) {
        return ResponseEntity.ok(ubicacionService.guardar(ubicacion));
    }
}
