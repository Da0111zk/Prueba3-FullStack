package com.example.kardex.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.kardex.dto.MovimientoRequestDTO;
import com.example.kardex.dto.MovimientoResponseDTO;
import com.example.kardex.dto.StockResponseDTO;
import com.example.kardex.service.KardexService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/Kardex/v2")
@RequiredArgsConstructor
@Tag(name = "Kardex V2", description = "Controlador unificado para la gestión de Inventario y Movimientos")
public class KardexController2 {

    private final KardexService kardexService;

    @GetMapping("/listar")
    @Operation(summary = "Obtener historial de movimientos", description = "Trae la lista completa de todas las auditorías de entrada y salida")
    public ResponseEntity<List<MovimientoResponseDTO>> obtenerMovimientos() {
        return ResponseEntity.ok(kardexService.obtenerTodosLosMovimientos());
    }

    @GetMapping("/movimientos")
    @Operation(summary = "Consultar Stock Actual", description = "Busca cuántas existencias quedan de un producto en una bodega")
    public ResponseEntity<StockResponseDTO> obtenerStock(@RequestParam Long productoId, @RequestParam Long bodegaId) {
        return ResponseEntity.ok(kardexService.obtenerStock(productoId, bodegaId));
    }

    @PostMapping("/movimiento")
    @Operation(summary = "Registrar movimiento", description = "Registra un nuevo INGRESO, EGRESO o AJUSTE en el Kardex")
    public ResponseEntity<MovimientoResponseDTO> crearMovimiento(@Valid @RequestBody MovimientoRequestDTO dto) {
        MovimientoResponseDTO nueva = kardexService.registrarMovimiento(dto);
        return ResponseEntity.status(201).body(nueva);
    }

    @PutMapping("/stock")
    @Operation(summary = "Actualizar Stock Manualmente", description = "Fuerza o corrige la cantidad de stock de un producto específico")
    public ResponseEntity<StockResponseDTO> actualizarStockManual(
            @RequestParam Long productoId, 
            @RequestParam Long bodegaId, 
            @RequestParam Integer nuevaCantidad) {
        return ResponseEntity.ok(kardexService.actualizarStockManual(productoId, bodegaId, nuevaCantidad));
    }

    @DeleteMapping("/stock")
    @Operation(summary = "Eliminar Registro de Stock", description = "Elimina de la base de datos la fila de existencias de un producto en una bodega")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Registro de stock eliminado de forma exitosa"),
        @ApiResponse(responseCode = "404", description = "Registro de stock no encontrado")
    })

    public ResponseEntity<Void> eliminar(@RequestParam Long productoId, @RequestParam Long bodegaId) {
        kardexService.eliminarStock(productoId, bodegaId);
        return ResponseEntity.noContent().build();
    }
}