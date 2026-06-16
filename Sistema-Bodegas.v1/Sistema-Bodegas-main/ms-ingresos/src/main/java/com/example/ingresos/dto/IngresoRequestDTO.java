package com.example.ingresos.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class IngresoRequestDTO {

    @NotNull(message = "El productoId es obligatorio")
    @Positive(message = "El productoId debe ser positivo")
    private Long productoId;

    @NotNull(message = "El proveedorId es obligatorio")
    @Positive(message = "El proveedorId debe ser positivo")
    private Long proveedorId;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    private Integer cantidad;

    @NotBlank(message = "El numero de guia es obligatorio")
    @Size(max = 50, message = "El numero de guia no puede exceder 50 caracteres")
    private String numeroGuia;

    @Size(max = 300, message = "Las observaciones no pueden exceder 300 caracteres")
    private String observaciones;

    @NotNull(message = "El ID de bodega es obligatorio")
    @Positive(message = "El ID de bodega debe ser positivo")
    private Long bodegaId;
}