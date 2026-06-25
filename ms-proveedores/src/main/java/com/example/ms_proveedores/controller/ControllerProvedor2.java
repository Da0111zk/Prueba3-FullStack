
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
import com.example.ms_proveedores.service.ProvedorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/Provedor/v2") 
@RequiredArgsConstructor
@Tag(name = "Provedor V2", description = "Controlador de prueba para la gestión de provedores con Swagger")
public class ControllerProvedor2 {

    private final ProvedorService provedorService;

    @GetMapping("/gestionar")
    @Operation(summary = "Obtener provedores", description = "Lista todos los provedores registrados")
    @ApiResponse(responseCode = "200", description = "Lista obtenida con éxito")
    public ResponseEntity<List<DtoResponseProvedor>> obtenerProvedores() {
        return ResponseEntity.ok(provedorService.obtenerProvedores());
    }

    @GetMapping("/{provedorId}")
    @Operation(summary = "Obtener provedor por ID", description = "Busca un provedor específico usando su identificador único")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Provedor encontrado", 
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DtoResponseProvedor.class))),
        @ApiResponse(responseCode = "404", description = "Provedor no encontrado")
    })
    public ResponseEntity<DtoResponseProvedor> obtenerPorId(@PathVariable Long provedorId) {
        return provedorService.obtenerPorId1(provedorId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear provedor", description = "Registra un nuevo provedor en el sistema")
    @ApiResponse(responseCode = "201", description = "Provedor  creado exitosamente",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = DtoResponseProvedor.class)))
    public ResponseEntity<DtoResponseProvedor> crear(@Valid @RequestBody DtoResponseProvedor dtoResponseProvedor) {
         DtoResponseProvedor nuevo = provedorService.guardar(dtoResponseProvedor);
        return ResponseEntity.status(201).body(nuevo);
    }

    @PutMapping("/{provedorId}") // Unificado a "provedorId" en español
    @Operation(summary = "Actualizar un provedor", description = "Actualiza los datos de un provedor existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Provedor actualizado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DtoResponseProvedor.class))),
        @ApiResponse(responseCode = "404", description = "Provedor no encontrado")
    })
    public ResponseEntity<DtoResponseProvedor> actualizar(@PathVariable Long provedorId, @Valid @RequestBody DtoResponseProvedor dtoResponseProvedor) {
        // CORRECCIÓN: Sincronizado perfectamente con el método .actualizar() de tu ProvedorService
        return provedorService.actualizar(provedorId, dtoResponseProvedor)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{provedorId}") // Unificado a "provedorId" en español
    @Operation(summary = "Eliminar un provedor", description = "Elimina un provedor del sistema por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Provedor eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Provedor no encontrado")
    })
    public ResponseEntity<Void> eliminar(@PathVariable Long provedorId) {
        // CORRECCIÓN: Sincronizado para usar la misma variable de la ruta
        if (provedorService.obtenerPorId(provedorId).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        provedorService.eliminar(provedorId);
        return ResponseEntity.noContent().build(); 
    }


}







   

   
