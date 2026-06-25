
package com.example.ms_proveedores.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "proveedor")
public class Proveedor{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long  provedorId;

    
    @Column(name = "razon_social", nullable = false, length = 100)
    private String razonSocial;

   
    @Column(name = "rut", nullable = false, unique = true)
    private int rut;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "telefono")
    private int telefono;

   
    @Column(name = "estado", nullable = false, length = 50)
    private String estado;



}
