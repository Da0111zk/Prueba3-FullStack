
package com.example.ms_proveedores.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "provedores")
public class Proveedor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long  provedorId;

    @NotBlank(message = "La razón social es obligatoria")
    @Column(name = "razon_social", nullable = false, length = 100)
    private String razonSocial;

    @NotBlank(message = "El RUT es obligatorio")
    @Column(name = "rut", nullable = false, unique = true)
    private int rut;
    @Size(max = 100, message = "El email no puede tener más de 100 caracteres")
    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "telefono")
    private int telefono;

    @NotBlank(message = "El estado es obligatorio")
    @Column(name = "estado", nullable = false, length = 50)
    private String estado;



}
