package com.example.kardex.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MovimientoResponseDTO {
    private Long id;
    private Long productoId;
    private Long bodegaId;
    private String tipoMovimiento;
    private Integer cantidad;
    private String signo;
    private Integer stockAnterior;
    private Integer stockNuevo;
    private LocalDateTime fechaMovimiento;
    private String referencia;
}