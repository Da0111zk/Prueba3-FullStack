package com.example.ms_ubicaciones.controller;


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

import com.example.ms_ubicaciones.dto.DtoResponseUbi;
import com.example.ms_ubicaciones.dto.DtoUbi;
import com.example.ms_ubicaciones.service.UbicacionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/ubicacion")
@RequiredArgsConstructor
public class UbicacionController {
    private final UbicacionService ubicacionService;

        
    @GetMapping("/listas")
    public ResponseEntity<List<DtoResponseUbi>> obtenerUbicaciones() {
        return ResponseEntity.ok(ubicacionService.obtenerUbicaciones());
    }



    @GetMapping("/{ubicacionid}")
    public ResponseEntity<DtoResponseUbi> obtenerPorId(@PathVariable Long ubicacionId) {
        return ubicacionService.obtenerPorId(ubicacionId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<DtoResponseUbi> crear(@Valid @RequestBody DtoUbi dtoUbi){
        DtoResponseUbi nueva = ubicacionService.guardar(dtoUbi);
        return ResponseEntity.status(201).body(nueva);
    }

    @PutMapping("/{ubicacionId}")
    public ResponseEntity<DtoResponseUbi> actualizar(@PathVariable Long ubicacionId, @Valid @RequestBody DtoUbi dtoUbi) {
        return ubicacionService.obtenerPorId(ubicacionId)
                .map(existente -> {
                    dtoUbi.setUbicacionId(ubicacionId);
                    return ResponseEntity.ok(ubicacionService.guardar(dtoUbi));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{ubicacionId}")
    public ResponseEntity<Void> eliminar(@PathVariable Long ubicacionId) {
        if (ubicacionService.obtenerPorId(ubicacionId).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        ubicacionService.eliminar(ubicacionId );
        return ResponseEntity.noContent().build(); 
    }


}
