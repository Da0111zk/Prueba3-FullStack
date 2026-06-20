package com.example.ubicaciones.modelo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ubicaciones")
@Schema(description = "Entidad que representa una ubicación o bodega")
public class Ubicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID de la ubicación", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long ubicacionId;

    @NotBlank(message = "La razón social es obligatoria")
    @Size(max = 100, message = "La razón social no puede superar los 100 caracteres")
    @Column(name = "razon_social", nullable = false, length = 100)
    @Schema(description = "Razón social asociada a la ubicación", example = "Proveedor Logística Norte")
    private String razonSocial;

    @NotBlank(message = "El pasillo es obligatorio")
    @Size(max = 50, message = "El pasillo no puede superar los 50 caracteres")
    @Column(name = "pasillo", nullable = false)
    @Schema(description = "Pasillo o sector físico", example = "Pasillo A-3")
    private String pasillo;

    @Pattern(regexp = "^[0-9]{8,15}$", message = "El teléfono debe contener solo números y tener entre 8 y 15 dígitos")
    @Column(name = "telefono", nullable = false)
    @Schema(description = "Teléfono de contacto", example = "56912345678")
    private Integer telefono;

    @NotBlank(message = "El RUT es obligatorio")
    @Size(max = 20, message = "El RUT no puede superar los 20 caracteres")
    @Column(name = "rut", nullable = false)
    @Schema(description = "RUT asociado a la ubicación o proveedor", example = "76.123.456-7")
    private String rut;

    @NotBlank(message = "El estado activo es obligatorio")
    @Pattern(regexp = "^(SI|NO|ACTIVO|INACTIVO)$", message = "El campo activo debe ser SI, NO, ACTIVO o INACTIVO")
    @Column(name = "activo", nullable = false)
    @Schema(description = "Estado de la ubicación", example = "ACTIVO")
    private String activo;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener un formato válido")
    @Size(max = 100, message = "El email no puede superar los 100 caracteres")
    @Column(name = "email", nullable = false)
    @Schema(description = "Correo electrónico de contacto", example = "contacto@empresa.cl")
    private String email;
}