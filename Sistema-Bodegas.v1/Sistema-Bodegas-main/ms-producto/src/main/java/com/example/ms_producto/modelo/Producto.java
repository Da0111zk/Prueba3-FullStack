package com.example.ms_producto.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Productos")
public class Producto {

    @Schema(description = "Identificador único del producto", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productoId;

    @Schema(description = "Nombre del producto", example = "Martillo")
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 200, message = "El nombre no puede superar 200 caracteres")
    @Column(nullable = false, length = 200)
    private String nombre;

    @Schema(description = "SKU del producto", example = "SKU001")
    @NotBlank(message = "El SKU no puede estar vacío")
    @Column(nullable = false, unique = true, length = 20)
    private String sku;

    @Schema(description = "Precio del producto", example = "19.99")
    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor que 0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Schema(description = "Estado del producto", example = "DISPONIBLE")
    @NotBlank(message = "El estado no puede estar vacío")
    @Column(nullable = false, length = 20)
    private String estado;

    @Schema(description = "ID de la categoría a la que pertenece el producto", example = "1")
    @NotNull(message = "El ID de la categoría es obligatorio")
    @Column(name = "categoria_id", nullable = false)
    private Long categoriaId; 

}
