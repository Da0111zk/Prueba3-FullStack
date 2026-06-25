package com.example.ms_ubicaciones.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DtoResponseUbi {

    private Long ubicacionId;
    private String razonSocial;
    private String pasillo;
    private Integer telefono;
    private Integer rut;
    private String activo;
    private String email;

}
