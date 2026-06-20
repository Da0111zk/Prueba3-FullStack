package com.example.ajustes.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AjusteRequestDTO {

    @Schema(description = "ID del producto a ajustar", example = "1")
    @NotNull(message = "El ID de producto es obligatorio")
    @Positive(message = "El ID de producto debe ser positivo")
    private Long productoId;

    @Schema(description = "ID de la bodega donde se realiza el ajuste", example = "1")
    @NotNull(message = "El ID de bodega es obligatorio")
    @Positive(message = "El ID de bodega debe ser positivo")
    private Long bodegaId;

    @Schema(description = "Cantidad del ajuste (positiva o negativa)", example = "-5")
    @NotNull(message = "La cantidad de ajuste es obligatoria")
    private Integer cantidadAjuste;

    @Schema(description = "Motivo del ajuste", example = "Diferencia detectada en conteo físico")
    @NotBlank(message = "El motivo es obligatorio")
    @Size(max = 200, message = "El motivo no puede exceder 200 caracteres")
    private String motivo;

    @Schema(description = "Responsable que solicita el ajuste", example = "Juan Pérez")
    @NotBlank(message = "El responsable es obligatorio")
    @Size(max = 100, message = "El responsable no puede exceder 100 caracteres")
    private String responsable;

    @Schema(description = "Observaciones adicionales del ajuste", example = "Conteo realizado el 15/06/2026")
    @Size(max = 300, message = "Las observaciones no pueden exceder 300 caracteres")
    private String observaciones;
}