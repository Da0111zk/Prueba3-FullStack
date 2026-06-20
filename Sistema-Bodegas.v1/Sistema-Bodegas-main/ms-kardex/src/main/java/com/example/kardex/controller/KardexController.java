package com.example.kardex.controller;

import com.example.kardex.dto.MovimientoRequestDTO;
import com.example.kardex.dto.MovimientoResponseDTO;
import com.example.kardex.dto.StockResponseDTO;
import com.example.kardex.service.KardexService;
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
@RequestMapping("/api/kardex")
@RequiredArgsConstructor
@Tag(name = "Kardex", description = "CRUD de movimientos y consulta de stock")
public class KardexController {

    private final KardexService service;

    @Operation(summary = "Listar movimientos")
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping("/movimientos")
    public ResponseEntity<List<MovimientoResponseDTO>> listarMovimientos() {
        return ResponseEntity.ok(service.listarMovimientos());
    }

    @Operation(summary = "Obtener movimiento por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movimiento encontrado",
                    content = @Content(schema = @Schema(implementation = MovimientoResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Movimiento no encontrado", content = @Content)
    })
    @GetMapping("/movimientos/{id}")
    public ResponseEntity<MovimientoResponseDTO> obtenerMovimientoPorId(
            @Parameter(description = "ID del movimiento", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerMovimientoPorId(id));
    }

    @Operation(summary = "Registrar movimiento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Movimiento creado",
                    content = @Content(schema = @Schema(implementation = MovimientoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content)
    })
    @PostMapping("/movimientos")
    public ResponseEntity<MovimientoResponseDTO> registrarMovimiento(
            @Valid @RequestBody MovimientoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registrarMovimiento(dto));
    }

    @Operation(summary = "Actualizar movimiento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movimiento actualizado",
                    content = @Content(schema = @Schema(implementation = MovimientoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Movimiento no encontrado", content = @Content)
    })
    @PutMapping("/movimientos/{id}")
    public ResponseEntity<MovimientoResponseDTO> actualizarMovimiento(
            @Parameter(description = "ID del movimiento", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody MovimientoRequestDTO dto) {
        return ResponseEntity.ok(service.actualizarMovimiento(id, dto));
    }

    @Operation(summary = "Eliminar movimiento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Movimiento eliminado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Movimiento no encontrado", content = @Content),
            @ApiResponse(responseCode = "400", description = "No se pudo revertir el stock", content = @Content)
    })
    @DeleteMapping("/movimientos/{id}")
    public ResponseEntity<Void> eliminarMovimiento(
            @Parameter(description = "ID del movimiento", example = "1")
            @PathVariable Long id) {
        service.eliminarMovimiento(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Obtener stock actual por producto y bodega")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock encontrado",
                    content = @Content(schema = @Schema(implementation = StockResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Stock no encontrado", content = @Content)
    })
    @GetMapping("/stock/producto/{productoId}/bodega/{bodegaId}")
    public ResponseEntity<StockResponseDTO> obtenerStock(
            @Parameter(description = "ID del producto", example = "100")
            @PathVariable Long productoId,
            @Parameter(description = "ID de la bodega", example = "1")
            @PathVariable Long bodegaId) {
        return ResponseEntity.ok(service.obtenerStock(productoId, bodegaId));
    }
}