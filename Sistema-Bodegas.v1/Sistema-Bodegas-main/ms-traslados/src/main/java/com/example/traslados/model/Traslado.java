package com.example.traslados.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "TRASLADO")
public class Traslado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "PRODUCTO_ID", nullable = false)
    private Long productoId;

    @Column(name = "UBIC_ORIGEN", nullable = false)
    private Long ubicOrigen;

    @Column(name = "UBIC_DESTINO", nullable = false)
    private Long ubicDestino;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "FECHA_TRASLADO", nullable = false)
    private LocalDateTime fecha;

    @Column(nullable = false, length = 255)
    private String motivo;

    @PrePersist
    protected void onCreate() {
        this.fecha = LocalDateTime.now();
    }
}