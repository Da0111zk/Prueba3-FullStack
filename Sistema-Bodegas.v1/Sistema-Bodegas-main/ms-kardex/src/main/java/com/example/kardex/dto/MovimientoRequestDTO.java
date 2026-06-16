package com.example.kardex.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MovimientoRequestDTO {

    @NotNull(message = "El ID de producto es obligatorio")
    @Positive(message = "El ID de producto debe ser positivo")
    private Long productoId;

    @NotNull(message = "El ID de bodega es obligatorio")
    @Positive(message = "El ID de bodega debe ser positivo")
    private Long bodegaId;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    private Integer cantidad;

    @NotBlank(message = "El tipoMovimiento es obligatorio")
    @Pattern(regexp = "INGRESO|EGRESO|AJUSTE", message = "tipoMovimiento debe ser INGRESO, EGRESO o AJUSTE")
    private String tipoMovimiento;

    @Pattern(regexp = "POSITIVO|NEGATIVO", message = "signo debe ser POSITIVO o NEGATIVO")
    private String signo;

    @Size(max = 100, message = "La referencia no puede exceder 100 caracteres")
    private String referencia;
}