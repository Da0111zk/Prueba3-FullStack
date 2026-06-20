package com.example.ajustes.controller;

import com.example.ajustes.dto.AjusteRequestDTO;
import com.example.ajustes.dto.AjusteResponseDTO;
import com.example.ajustes.service.AjusteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "Ajustes", description = "Registro de ajustes de inventario por diferencias detectadas en conteos")
@RestController
@RequestMapping("/api/ajustes")
public class AjusteController {

    @Autowired
    private AjusteService service;


    @Operation(summary = "Registrar un nuevo ajuste de inventario")
    @ApiResponses({
    @ApiResponse(responseCode = "201", description = "Ajuste registrado correctamente"),
    @ApiResponse(responseCode = "400", description = "Datos del ajuste inválidos")
    })
    @PostMapping
    public ResponseEntity<AjusteResponseDTO> crear(@Valid @RequestBody AjusteRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto));
    }

    @Operation(summary = "Listar todos los ajustes", description = "Retorna el historial completo de ajustes registrados")
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
   @GetMapping
    public ResponseEntity<List<AjusteResponseDTO>> listarTodos() {
    System.out.println("Entró al endpoint listarTodos");
    return ResponseEntity.ok(service.listarTodos());
}

    @Operation(summary = "Buscar ajuste por ID")
    @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Ajuste encontrado"),
    @ApiResponse(responseCode = "404", description = "Ajuste no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AjusteResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Operation(summary = "Listar ajustes en estado PENDIENTE")
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping("/pendientes")
    public ResponseEntity<List<AjusteResponseDTO>> listarPendientes() {
        return ResponseEntity.ok(service.listarPendientes());
    }

    @Operation(summary = "Aprobar un ajuste pendiente", description = "Aplica el ajuste sobre el kardex al ser aprobado")
    @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Ajuste aprobado correctamente"),
    @ApiResponse(responseCode = "404", description = "Ajuste no encontrado")
    })
    @PutMapping("/{id}/aprobar")
    public ResponseEntity<AjusteResponseDTO> aprobar(@PathVariable Long id) {
        return ResponseEntity.ok(service.aprobar(id));
    }

    @Operation(summary = "Rechazar un ajuste pendiente")
    @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Ajuste rechazado correctamente"),
    @ApiResponse(responseCode = "404", description = "Ajuste no encontrado")
    })
    @PutMapping("/{id}/rechazar")
    public ResponseEntity<AjusteResponseDTO> rechazar(@PathVariable Long id) {
        return ResponseEntity.ok(service.rechazar(id));
    }
    
}
