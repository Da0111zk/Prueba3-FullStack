package com.example.ajustes.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AjusteResponseDTO {
    private Long id;
    private Long productoId;
    private Long bodegaId;
    private Integer cantidadAjuste;
    private Integer stockAnterior;
    private Integer stockNuevo;
    private LocalDate fechaAjuste;
    private String motivo;
    private String responsable;
    private String estado;
    private String observaciones;
}