package com.example.ms_proveedores.controller;
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

import com.example.ms_proveedores.dto.DtoResponseProvedor;
import com.example.ms_proveedores.modelo.Proveedor;
import com.example.ms_proveedores.service.ProvedorService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping("/api/provedor")
@RequiredArgsConstructor
public class ProvedorController {
    private final ProvedorService provedorService;

    @GetMapping("/gestion")
    public ResponseEntity<List<DtoResponseProvedor>> obtenerProvedores() {
        return ResponseEntity.ok(provedorService.obtenerProvedores());
    }

    @GetMapping("/id")
    public ResponseEntity<DtoResponseProvedor> obtenerPorId(@PathVariable Long id) {
        return provedorService.obtenerProvedores(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build()); 
    }

    @PostMapping
    public ResponseEntity<Proveedor> crear(@Valid @RequestBody Proveedor provedor){
        Proveedor nuevo = provedorService.guardar(provedor);
        return ResponseEntity.status(201).body(nuevo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Proveedor> actualizar(@PathVariable Long id, @Valid @RequestBody Proveedor datos) {
        return provedorService.obtenerPorId(id)
                .map(existente -> {
                    datos.setProvedorId(id); 
                    return ResponseEntity.ok(provedorService.guardar(datos));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (provedorService.obtenerPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        provedorService.eliminar(id);
        return ResponseEntity.noContent().build(); 
    }

    
}