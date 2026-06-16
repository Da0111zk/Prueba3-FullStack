package com.example.ubicaciones.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DtoUbi {
    
    @NotBlank(message = "El nombre de la ubicación es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres")
    private Long ubicacionId;

    @NotBlank(message = "El nombre de la ubicación es obligatorio")
    @Size(max = 100, message = "La razón social no puede exceder los 100 caracteres")
    private String razonSocial;

  
    @NotBlank(message = "El pasillo es obligatorio")
    @Size(max = 100, message = "El pasillo no puede exceder los 100 caracteres")
    private String pasillo;

   
    @NotBlank(message = "El teléfono es obligatorio")
    @Size(max = 20, message = "El teléfono no puede exceder los 20 caracteres")
    private int telefono;

  
    @NotBlank(message = "El RUT es obligatorio")
    @Size(max = 20, message = "El RUT no puede exceder los 20 caracteres")
    private int rut;

    @NotBlank(message = "El estado es obligatorio")
    private String activo;


    @NotBlank(message = "El email es obligatorio")
    @Size(max = 100, message = "El email no puede exceder los 100 caracteres")
    private String email;

}
