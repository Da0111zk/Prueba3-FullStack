package com.example.ms_categorias.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DtoResponseCategoria {
    private Long categoriaId;
    private String nombre;
    private String descripcion;
    private String estado;

}
