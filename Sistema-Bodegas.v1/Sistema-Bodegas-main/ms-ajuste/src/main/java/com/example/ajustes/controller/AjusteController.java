package com.example.ajustes.controller;

import com.example.ajustes.dto.AjusteRequestDTO;
import com.example.ajustes.dto.AjusteResponseDTO;
import com.example.ajustes.service.AjusteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ajustes")
public class AjusteController {

    @Autowired
    private AjusteService service;

    @PostMapping
    public ResponseEntity<AjusteResponseDTO> crear(@Valid @RequestBody AjusteRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto));
    }

   @GetMapping
    public ResponseEntity<List<AjusteResponseDTO>> listarTodos() {
    System.out.println("Entró al endpoint listarTodos");
    return ResponseEntity.ok(service.listarTodos());
}

    @GetMapping("/{id}")
    public ResponseEntity<AjusteResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/pendientes")
    public ResponseEntity<List<AjusteResponseDTO>> listarPendientes() {
        return ResponseEntity.ok(service.listarPendientes());
    }

    @PutMapping("/{id}/aprobar")
    public ResponseEntity<AjusteResponseDTO> aprobar(@PathVariable Long id) {
        return ResponseEntity.ok(service.aprobar(id));
    }

    @PutMapping("/{id}/rechazar")
    public ResponseEntity<AjusteResponseDTO> rechazar(@PathVariable Long id) {
        return ResponseEntity.ok(service.rechazar(id));
    }
    
    @GetMapping("/test")
    public ResponseEntity<String> test() {
    return ResponseEntity.ok("ok");
}
}