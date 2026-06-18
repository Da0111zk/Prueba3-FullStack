package com.example.traslados.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TrasladoResponseDTO {
    private Long id;
    private Long productoId;
    private Long ubicOrigen;
    private Long ubicDestino;
    private Integer cantidad;
    private LocalDateTime fecha;
    private String motivo;
}