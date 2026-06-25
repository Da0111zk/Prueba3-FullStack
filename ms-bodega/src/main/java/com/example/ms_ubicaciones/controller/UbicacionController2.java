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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/ubicaciones/v2") // Cambiado a 2 para evitar choques con tu controlador viejo
@RequiredArgsConstructor
@Tag(name = "Ubicaciones V2", description = "Controlador de prueba para la gestión de ubicaciones con Swagger")
public class UbicacionController2 {

    private final UbicacionService ubicacionService;


    @GetMapping("/listar")
    @Operation(summary = "Obtener ubicaciones") // Deja solo esto, quita el @ApiResponse complejo por ahora
    public ResponseEntity<List<DtoResponseUbi>> obtenerUbicaciones() {
        return ResponseEntity.ok(ubicacionService.obtenerUbicaciones());
    }

    @GetMapping("/{ubicacionId}") // Limpiado el duplicado que tenías aquí
    @Operation(summary = "Obtener ubicación por ID", description = "Busca una ubicación específica usando su identificador único")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ubicación encontrada", 
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DtoResponseUbi.class))),
        @ApiResponse(responseCode = "404", description = "Ubicación no encontrada")
    })
    public ResponseEntity<DtoResponseUbi> obtenerPorId(@PathVariable Long ubicacionId) {
        return ubicacionService.obtenerPorId(ubicacionId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear ubicación", description = "Registra una nueva ubicación en el sistema")
    @ApiResponse(responseCode = "201", description = "Ubicación creada exitosamente",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = DtoResponseUbi.class)))
    public ResponseEntity<DtoResponseUbi> crear(@Valid @RequestBody DtoUbi dtoUbi){
        DtoResponseUbi nueva = ubicacionService.guardar(dtoUbi);
        return ResponseEntity.status(201).body(nueva);
    }

    @PutMapping("/{ubicacionId}")
        @Operation(summary = "Actualizar una ubicación", description = "Actualiza los datos de una ubicación existente")
        public ResponseEntity<DtoResponseUbi> actualizar(@PathVariable Long ubicacionId, @Valid @RequestBody DtoUbi dtoUbi) {
            return ubicacionService.obtenerPorId(ubicacionId)
                    .map(existente -> {
                        // Forzamos el ID de la URL en el DTO antes de mandar a guardar para que sea un UPDATE real
                        dtoUbi.setUbicacionId(ubicacionId); 
                        return ResponseEntity.ok(ubicacionService.guardar(dtoUbi)); 
                    })
                    .orElse(ResponseEntity.notFound().build());
    }
    
    
    @DeleteMapping("/{ubicacionId}")
    @Operation(summary = "Eliminar una ubicación", description = "Elimina una ubicación del sistema por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Ubicación personalizada eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Ubicación no encontrada")
    })
    public ResponseEntity<Void> eliminar(@PathVariable Long ubicacionId) {
        if (ubicacionService.obtenerPorId(ubicacionId).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        ubicacionService.eliminar(ubicacionId);
        return ResponseEntity.noContent().build(); 
    }
}