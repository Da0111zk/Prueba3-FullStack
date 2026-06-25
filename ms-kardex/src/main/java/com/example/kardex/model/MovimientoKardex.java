package com.example.kardex.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "KARDEX_MOVIMIENTOS")
public class MovimientoKardex {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_kardex")
    @SequenceGenerator(name = "seq_kardex", sequenceName = "SEQ_KARDEX", allocationSize = 1)
    private Long id;

    @Column(name = "PRODUCTO_ID", nullable = false)
    private Long productoId;

    @Column(name = "BODEGA_ID", nullable = false)
    private Long bodegaId;

    @Column(name = "TIPO_MOVIMIENTO", nullable = false, length = 20)
    private String tipoMovimiento;

    @Column(name = "CANTIDAD", nullable = false)
    private Integer cantidad;

    @Column(name = "SIGNO", length = 10)
    private String signo;

    @Column(name = "STOCK_ANTERIOR", nullable = false)
    private Integer stockAnterior;

    @Column(name = "STOCK_NUEVO", nullable = false)
    private Integer stockNuevo;

    @Column(name = "FECHA_MOVIMIENTO", nullable = false)
    private LocalDate fechaMovimiento;

    @Column(name = "REFERENCIA", length = 100)
    private String referencia;
}