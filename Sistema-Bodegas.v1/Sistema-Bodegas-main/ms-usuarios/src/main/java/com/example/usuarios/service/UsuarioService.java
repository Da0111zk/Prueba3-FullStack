package com.example.usuarios.service;

import com.example.usuarios.dto.UsuarioRequestDTO;
import com.example.usuarios.dto.UsuarioResponseDTO;
import com.example.usuarios.exception.RecursoNoEncontradoException;
import com.example.usuarios.model.Usuario;
import com.example.usuarios.repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    public List<UsuarioResponseDTO> listarTodos() {
        log.info("Listando usuarios activos");
        return repository.findByActivoTrue()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public UsuarioResponseDTO obtenerPorId(Long id) {
        log.info("Buscando usuario por id {}", id);
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con id: " + id));
        return toResponseDTO(usuario);
    }

    public List<UsuarioResponseDTO> obtenerPorRol(String rol) {
        log.info("Buscando usuarios por rol {}", rol);
        return repository.findByRolAndActivoTrue(rol)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public UsuarioResponseDTO crear(UsuarioRequestDTO dto) {
        validarEmailUnico(dto.getEmail());

        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(dto.getPassword());
        usuario.setRol(dto.getRol());
        usuario.setActivo(true);

        log.info("Creando usuario con email {}", dto.getEmail());
        return toResponseDTO(repository.save(usuario));
    }

    public UsuarioResponseDTO actualizar(Long id, UsuarioRequestDTO dto) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con id: " + id));

        if (!usuario.getEmail().equalsIgnoreCase(dto.getEmail()) && repository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Ya existe un usuario con el email: " + dto.getEmail());
        }

        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(dto.getPassword());
        usuario.setRol(dto.getRol());

        log.info("Actualizando usuario con id {}", id);
        return toResponseDTO(repository.save(usuario));
    }

    public void eliminar(Long id) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con id: " + id));

        usuario.setActivo(false);
        repository.save(usuario);

        log.info("Soft delete del usuario con id {}", id);
    }

    private void validarEmailUnico(String email) {
        if (repository.existsByEmail(email)) {
            throw new IllegalArgumentException("Ya existe un usuario con el email: " + email);
        }
    }

    private UsuarioResponseDTO toResponseDTO(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                usuario.getRol(),
                usuario.getActivo(),
                usuario.getFechaCreacion()
        );
    }
}