package com.example.traslados.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TrasladoRequestDTO {

    @NotNull(message = "El productoId es obligatorio")
    private Long productoId;

    @NotNull(message = "La ubicación origen es obligatoria")
    private Long ubicOrigen;

    @NotNull(message = "La ubicación destino es obligatoria")
    private Long ubicDestino;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    private Integer cantidad;

    @NotBlank(message = "El motivo no puede estar vacío")
    @Size(max = 255, message = "El motivo no puede superar 255 caracteres")
    private String motivo;
}