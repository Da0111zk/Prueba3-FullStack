package com.example.ms_ubicaciones.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DtoUbi {
    
  
   
    private Long ubicacionId;

    @NotBlank(message = "El nombre de la ubicación es obligatorio")
    @Size(max = 100, message = "La razón social no puede exceder los 100 caracteres")
    private String razonSocial;

    @NotBlank(message = "El pasillo es obligatorio")
    @Size(max = 100, message = "El pasillo no puede exceder los 100 caracteres")
    private String pasillo;

    @NotNull(message = "El teléfono es obligatorio")
  
    private int telefono;

    @NotNull(message = "El RUT es obligatorio")
   
    private int rut;

    @NotBlank(message = "El estado es obligatorio")
    private String activo;

    @NotBlank(message = "El email es obligatorio")
    @Size(max = 100, message = "El email no puede exceder los 100 caracteres")
    private String email;
}