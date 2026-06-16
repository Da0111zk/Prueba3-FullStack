package com.example.usuarios.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsuarioRequestDTO {

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido no puede estar vacío")
    @Size(max = 100, message = "El apellido no puede superar 100 caracteres")
    private String apellido;

    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El email no es válido")
    @Size(max = 150, message = "El email no puede superar 150 caracteres")
    private String email;

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 8, max = 255, message = "La contraseña debe tener al menos 8 caracteres")
    private String password;

    @NotBlank(message = "El rol no puede estar vacío")
    @Pattern(regexp = "ADMIN|BODEGUERO|SUPERVISOR",
            message = "El rol debe ser ADMIN, BODEGUERO o SUPERVISOR")
    private String rol;
}