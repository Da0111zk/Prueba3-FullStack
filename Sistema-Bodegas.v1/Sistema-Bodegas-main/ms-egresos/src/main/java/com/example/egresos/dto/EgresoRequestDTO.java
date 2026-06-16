package com.example.egresos.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EgresoRequestDTO {

    @NotNull(message = "El ID de producto es obligatorio")
    @Positive(message = "El ID de producto debe ser positivo")
    private Long productoId;

    @NotNull(message = "El ID de bodega es obligatorio")
    @Positive(message = "El ID de bodega debe ser positivo")
    private Long bodegaId;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    private Integer cantidad;

    @NotBlank(message = "El destinatario es obligatorio")
    @Size(max = 100, message = "El destinatario no puede exceder 100 caracteres")
    private String destinatario;

    @Size(max = 50, message = "El número de documento no puede exceder 50 caracteres")
    private String numeroDocumento;

    @Size(max = 300, message = "Las observaciones no pueden exceder 300 caracteres")
    private String observaciones;
}