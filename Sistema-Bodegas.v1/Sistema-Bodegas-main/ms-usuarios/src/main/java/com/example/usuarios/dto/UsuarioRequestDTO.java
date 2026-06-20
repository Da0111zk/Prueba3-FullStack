package com.example.usuarios.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
public class UsuarioRequestDTO {

    @Schema(description = "Nombre del usuario", example = "Daniel")
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
    private String nombre;

    @Schema(description = "Apellido del usuario", example = "Pérez")
    @NotBlank(message = "El apellido no puede estar vacío")
    @Size(max = 100, message = "El apellido no puede superar 100 caracteres")
    private String apellido;

    @Schema(description = "Correo electrónico del usuario", example = "daniel.perez@empresa.cl")
    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El email no es válido")
    @Size(max = 150, message = "El email no puede superar 150 caracteres")
    private String email;

    @Schema(description = "Contraseña del usuario (mínimo 8 caracteres)", example = "Password123")
    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 8, max = 255, message = "La contraseña debe tener al menos 8 caracteres")
    private String password;

    @Schema(description = "Rol del usuario en el sistema", example = "BODEGUERO", allowableValues = {"ADMIN", "BODEGUERO", "SUPERVISOR"})
    @NotBlank(message = "El rol no puede estar vacío")
    @Pattern(regexp = "ADMIN|BODEGUERO|SUPERVISOR",
            message = "El rol debe ser ADMIN, BODEGUERO o SUPERVISOR")
    private String rol;
}