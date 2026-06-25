package com.example.ms_proveedores.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class DtoResponseProvedor {
    
    private Long provedorId;
    private String razonSocial;
    private int rut;
    private String email;
    private int telefono;
    private String estado;




}
