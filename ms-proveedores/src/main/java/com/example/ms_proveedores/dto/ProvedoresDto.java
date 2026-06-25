
package com.example.ms_proveedores.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProvedoresDto {
    @NotBlank(message = "El ID del proveedor es obligatorio")
    private Long proveedorId;

    @NotBlank(message = "La razón social es obligatoria")
    private String razonSocial;


    @NotBlank(message = "El RUT es obligatorio")
    private int rut;

    @NotBlank(message = "El Correo Electrónico del proveedor es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres")
    private String Email;

    @NotBlank(message = "El teléfono es obligatorio")
    private int Telefono;

    @NotBlank(message = "El estado es obligatorio")
    private String estado;  


  



  

}
