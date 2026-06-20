package com.example.ms_proveedores.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.ms_proveedores.modelo.Proveedor;
import com.example.ms_proveedores.service.ProveedorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/proveedores")
@RequiredArgsConstructor
@Tag(name = "Proveedores", description = "CRUD de proveedores")
public class ProveedorController {

    private final ProveedorService proveedorService;

    @Operation(summary = "Listar proveedores")
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping
    public ResponseEntity<List<Proveedor>> obtenerProveedores() {
        return ResponseEntity.ok(proveedorService.obtenerProveedores());
    }

    @Operation(summary = "Obtener proveedor por ID")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Proveedor encontrado",
            content = @Content(schema = @Schema(implementation = Proveedor.class))),
    @ApiResponse(responseCode = "404", description = "Proveedor no encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Proveedor> obtenerPorId(
        @Parameter(description = "ID del proveedor", example = "1")
        @PathVariable Long id) {
        return proveedorService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear proveedor")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Proveedor creado correctamente",
            content = @Content(schema = @Schema(implementation = Proveedor.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Proveedor> crear(@Valid @RequestBody Proveedor proveedor) {
        Proveedor nuevo = proveedorService.guardar(proveedor);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @Operation(summary = "Actualizar proveedor")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Proveedor actualizado correctamente",
        content = @Content(schema = @Schema(implementation = Proveedor.class))),
    @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content),
    @ApiResponse(responseCode = "404", description = "Proveedor no encontrado", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Proveedor> actualizar(
            @Parameter(description = "ID del proveedor", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody Proveedor datos) {
        return proveedorService.obtenerPorId(id)
                .map(existente -> {
                    datos.setProveedorId(id);
                    return ResponseEntity.ok(proveedorService.guardar(datos));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar proveedor")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "204", description = "Proveedor eliminado correctamente", content = @Content),
    @ApiResponse(responseCode = "404", description = "Proveedor no encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del proveedor", example = "1")
            @PathVariable Long id) {
        if (proveedorService.obtenerPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        proveedorService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}