package com.example.kardex.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "KARDEX_STOCK",
       uniqueConstraints = @UniqueConstraint(columnNames = {"PRODUCTO_ID", "BODEGA_ID"}))
public class StockActual {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_kardex_stock")
    @SequenceGenerator(name = "seq_kardex_stock", sequenceName = "SEQ_KARDEX_STOCK", allocationSize = 1)
    private Long id;

    @Column(name = "PRODUCTO_ID", nullable = false)
    private Long productoId;

    @Column(name = "BODEGA_ID", nullable = false)
    private Long bodegaId;

    @Column(name = "CANTIDAD", nullable = false)
    private Integer cantidad;
}