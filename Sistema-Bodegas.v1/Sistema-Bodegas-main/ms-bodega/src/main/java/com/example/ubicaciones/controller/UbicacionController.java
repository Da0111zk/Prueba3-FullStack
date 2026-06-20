package com.example.ubicaciones.controller;

import com.example.ubicaciones.modelo.Ubicacion;
import com.example.ubicaciones.service.UbicacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ubicaciones")
@RequiredArgsConstructor
@Tag(name = "Bodegas/Ubicaciones", description = "Gestión de ubicaciones físicas de bodega")
public class UbicacionController {

    private final UbicacionService ubicacionService;

    @Operation(summary = "Listar todas las ubicaciones", description = "Retorna el listado completo de ubicaciones registradas")
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping
    public ResponseEntity<List<Ubicacion>> obtenerUbicaciones() {
        return ResponseEntity.ok(ubicacionService.obtenerUbicaciones());
    }

    @Operation(summary = "Obtener ubicación por ID", description = "Retorna una ubicación específica según su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ubicación encontrada",
                    content = @Content(schema = @Schema(implementation = Ubicacion.class))),
            @ApiResponse(responseCode = "404", description = "Ubicación no encontrada", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Ubicacion> obtenerPorId(
            @Parameter(description = "ID de la ubicación", example = "1")
            @PathVariable("id") Long ubicacionId) {
        return ubicacionService.obtenerPorId(ubicacionId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Registrar ubicación", description = "Crea una nueva ubicación")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ubicación creada correctamente",
                    content = @Content(schema = @Schema(implementation = Ubicacion.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Ubicacion> guardar(@Valid @RequestBody Ubicacion ubicacion) {
        Ubicacion nuevaUbicacion = ubicacionService.guardar(ubicacion);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaUbicacion);
    }

    @Operation(summary = "Actualizar ubicación", description = "Actualiza una ubicación existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ubicación actualizada correctamente",
                    content = @Content(schema = @Schema(implementation = Ubicacion.class))),
            @ApiResponse(responseCode = "404", description = "Ubicación no encontrada", content = @Content),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Ubicacion> actualizar(
            @Parameter(description = "ID de la ubicación", example = "1")
            @PathVariable("id") Long ubicacionId,
            @Valid @RequestBody Ubicacion ubicacion) {
        return ubicacionService.actualizar(ubicacionId, ubicacion)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar ubicación", description = "Elimina una ubicación por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Ubicación eliminada correctamente", content = @Content),
            @ApiResponse(responseCode = "404", description = "Ubicación no encontrada", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID de la ubicación", example = "1")
            @PathVariable("id") Long ubicacionId) {
        boolean eliminado = ubicacionService.eliminar(ubicacionId);

        if (!eliminado) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}