package com.example.traslados.controller;

import com.example.traslados.dto.TrasladoRequestDTO;
import com.example.traslados.dto.TrasladoResponseDTO;
import com.example.traslados.service.TrasladoService;
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

@Tag(name = "Traslados", description = "Registro de traslados de productos entre bodegas/ubicaciones")
@RestController
@RequestMapping("/api/traslados")
public class TrasladoController {

    @Autowired
    private TrasladoService service;

    @Operation(summary = "Listar todos los traslados", description = "Retorna el historial completo de traslados registrados")
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping
    public ResponseEntity<List<TrasladoResponseDTO>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @Operation(summary = "Obtener traslado por ID")
    @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Traslado encontrado"),
    @ApiResponse(responseCode = "404", description = "Traslado no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TrasladoResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @Operation(summary = "Listar traslados de un producto específico")
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<TrasladoResponseDTO>> obtenerPorProducto(@PathVariable Long productoId) {
        return ResponseEntity.ok(service.obtenerPorProducto(productoId));
    }

    @Operation(summary = "Registrar un nuevo traslado", description = "Valida que la bodega origen y destino sean distintas")
    @ApiResponses({
    @ApiResponse(responseCode = "201", description = "Traslado registrado correctamente"),
    @ApiResponse(responseCode = "400", description = "Datos inválidos o bodega origen igual a destino")
    })
    @PostMapping
    public ResponseEntity<TrasladoResponseDTO> crear(@Valid @RequestBody TrasladoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto));
    }

    @Operation(summary = "Actualizar un traslado existente")
    @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Traslado actualizado correctamente"),
    @ApiResponse(responseCode = "404", description = "Traslado no encontrado"),
    @ApiResponse(responseCode = "400", description = "Datos del traslado inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TrasladoResponseDTO> actualizar(@PathVariable Long id,
                                                          @Valid @RequestBody TrasladoRequestDTO dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }
}