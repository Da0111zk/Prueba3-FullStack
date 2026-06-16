

package com.example.ms_producto.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductoDto {
    private Long productoId;

    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres")
    private String nombre;

    @Size(max = 255, message = "La descripción no puede exceder los 255 caracteres")
    private String descripcion;

    @NotBlank(message = "El estado es obligatorio")
    private String sku;

    @NotBlank(message = "El estado es obligatorio")
    private int precio;

    @NotBlank(message = "El estado es obligatorio")
    private String estado;

}
