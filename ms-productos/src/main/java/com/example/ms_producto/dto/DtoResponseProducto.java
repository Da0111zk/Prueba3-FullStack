package com.example.ms_producto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DtoResponseProducto {
    private Long productoId;
    private String nombre;
    private String sku;
    private int precio;
    private String estado;


}
