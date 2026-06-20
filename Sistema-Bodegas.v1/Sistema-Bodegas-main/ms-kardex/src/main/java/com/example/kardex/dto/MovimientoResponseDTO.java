package com.example.kardex.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "Respuesta con el detalle de un movimiento de kardex")
public class MovimientoResponseDTO {

    @Schema(description = "ID del movimiento", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "ID del producto", example = "1001", accessMode = Schema.AccessMode.READ_ONLY)
    private Long productoId;

    @Schema(description = "ID de la bodega", example = "2", accessMode = Schema.AccessMode.READ_ONLY)
    private Long bodegaId;

    @Schema(description = "Tipo de movimiento", example = "INGRESO", accessMode = Schema.AccessMode.READ_ONLY)
    private String tipoMovimiento;

    @Schema(description = "Cantidad movida", example = "10", accessMode = Schema.AccessMode.READ_ONLY)
    private int cantidad;

    @Schema(description = "Signo usado si el movimiento es AJUSTE", example = "POSITIVO", accessMode = Schema.AccessMode.READ_ONLY)
    private String signo;

    @Schema(description = "Stock antes del movimiento", example = "25", accessMode = Schema.AccessMode.READ_ONLY)
    private int stockAnterior;

    @Schema(description = "Stock después del movimiento", example = "35", accessMode = Schema.AccessMode.READ_ONLY)
    private int stockNuevo;

    @Schema(description = "Fecha y hora del movimiento", example = "2026-06-20T00:20:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fechaMovimiento;

    @Schema(description = "Referencia asociada", example = "Ingreso por compra OC-123", accessMode = Schema.AccessMode.READ_ONLY)
    private String referencia;
}