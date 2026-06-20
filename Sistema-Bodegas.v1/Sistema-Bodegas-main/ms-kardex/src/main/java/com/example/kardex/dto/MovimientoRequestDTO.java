package com.example.kardex.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Datos de entrada para registrar o actualizar un movimiento de kardex")
public class MovimientoRequestDTO {

    @NotNull(message = "El productoId es obligatorio")
    @Schema(description = "ID del producto", example = "1001", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long productoId;

    @NotNull(message = "El bodegaId es obligatorio")
    @Schema(description = "ID de la bodega", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long bodegaId;

    @NotBlank(message = "El tipoMovimiento es obligatorio")
    @Schema(
            description = "Tipo de movimiento",
            example = "INGRESO",
            allowableValues = {"INGRESO", "EGRESO", "AJUSTE"},
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String tipoMovimiento;

    @Positive(message = "La cantidad debe ser mayor que 0")
    @Schema(description = "Cantidad del movimiento", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
    private int cantidad;

    @Schema(
            description = "Signo del ajuste, solo cuando tipoMovimiento es AJUSTE",
            example = "POSITIVO",
            allowableValues = {"POSITIVO", "NEGATIVO"},
            nullable = true
    )
    private String signo;

    @Size(max = 255, message = "La referencia no puede superar los 255 caracteres")
    @Schema(description = "Referencia o comentario del movimiento", example = "Ingreso por compra OC-123")
    private String referencia;
}