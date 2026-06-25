package com.example.kardex.dto;

import lombok.Data;

import java.time.LocalDate;


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
    private LocalDate fechaMovimiento;
    private String referencia;
}