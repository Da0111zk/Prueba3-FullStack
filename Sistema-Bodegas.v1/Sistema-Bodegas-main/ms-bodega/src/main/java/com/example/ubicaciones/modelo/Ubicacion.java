package com.example.ubicaciones.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ubicaciones")
public class Ubicacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ubicacionId;

    @NotBlank
    @Column(name = "razon_social", nullable = false, length = 100)
    private String razonSocial;

    
    @Column(name = "pasillo", nullable = false)
    private String pasillo;

    @Column(name = "telefono", nullable = false)
    private int telefono;

    @Column(name = "rut", nullable = false)
    private String rut;

    @Column(name = "activo", nullable = false)
    private String activo;

    @Column(name = "email", nullable = false)
    private String email;


}