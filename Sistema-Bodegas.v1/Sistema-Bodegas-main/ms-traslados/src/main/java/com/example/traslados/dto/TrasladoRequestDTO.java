package com.example.traslados.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
public class TrasladoRequestDTO {
    @Schema(description = "ID del producto a trasladar", example = "1")
    @NotNull(message = "El productoId es obligatorio")
    private Long productoId;
    
    @Schema(description = "ID de la ubicación/bodega de origen", example = "1")
    @NotNull(message = "La ubicación origen es obligatoria")
    private Long ubicOrigen;

    @Schema(description = "ID de la ubicación/bodega de destino", example = "2")
    @NotNull(message = "La ubicación destino es obligatoria")
    private Long ubicDestino;

    @Schema(description = "Cantidad de unidades a trasladar", example = "15")
    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    private Integer cantidad;
    
    @Schema(description = "Motivo del traslado", example = "Reabastecimiento de sucursal")
    @NotBlank(message = "El motivo no puede estar vacío")
    @Size(max = 255, message = "El motivo no puede superar 255 caracteres")
    private String motivo;
}