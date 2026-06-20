
package com.example.ms_proveedores.modelo;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Table(name = "proveedores")
public class Proveedor {
    @Schema(description = "Identificador único del proveedor", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long  proveedorId;

    @Schema(description = "Razón social del proveedor", example = "Distribuidora ABC SPA")
    @NotBlank(message = "La razón social es obligatoria")
    @Column(name = "razon_social", nullable = false, length = 100)
    private String razonSocial;

    @Schema(description = "RUT del proveedor", example = "12345678-9")
    @NotBlank(message = "El RUT es obligatorio")
    @Column(name = "rut", nullable = false, unique = true)
    private int rut;

    @Schema(description = "Email de contacto del proveedor", example = "contacto@distribuidoraabc.cl")
    @Size(max = 100, message = "El email no puede tener más de 100 caracteres")
    @Column(name = "email", length = 100)
    private String email;

    @Schema(description = "Número de teléfono del proveedor", example = "987654321")
    @Column(name = "telefono")
    private int telefono;

    @Schema(description = "Estado del proveedor", example = "Activo")
    @NotBlank(message = "El estado es obligatorio")
    @Column(name = "estado", nullable = false, length = 50)
    private String estado;



}
