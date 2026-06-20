package com.example.ms_categorias.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DtoCategoria {
    

    private Long categoriaId;
    @Schema(description = "Nombre de la categoría", example = "Herramientas")
    @NotBlank(message = "El nombre de la categoría es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres")
    private String nombre;

    @Size(max = 255, message = "La descripción no puede exceder los 255 caracteres")
    private String descripcion;

    @Schema(description = "Estado de la categoría", example = "activo")
    @NotBlank(message = "El estado es obligatorio")
    private String estado; //o activo

}
    