package com.example.ajustes.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "AJUSTES")
public class Ajuste {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_ajustes")
    @SequenceGenerator(name = "seq_ajustes", sequenceName = "SEQ_AJUSTES", allocationSize = 1)
    private Long id;

    

    @Column(name = "PRODUCTO_ID", nullable = false)
    private Long productoId;

    @Column(name = "BODEGA_ID", nullable = false)
    private Long bodegaId;

    @Column(name = "CANTIDAD_AJUSTE", nullable = false)
    private Integer cantidadAjuste;

    @Column(name = "STOCK_ANTERIOR", nullable = false)
    private Integer stockAnterior;

    @Column(name = "STOCK_NUEVO", nullable = false)
    private Integer stockNuevo;

    @Column(name = "FECHA_AJUSTE", nullable = false)
    private LocalDate fechaAjuste;

    @Column(name = "MOTIVO", nullable = false, length = 200)
    private String motivo;

    @Column(name = "RESPONSABLE", nullable = false, length = 100)
    private String responsable;

    @Column(name = "ESTADO", nullable = false, length = 20)
    private String estado = "PENDIENTE";

    @Column(name = "OBSERVACIONES", length = 300)
    private String observaciones;
}