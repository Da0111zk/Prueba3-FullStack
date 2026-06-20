package com.example.usuarios.service;

import com.example.usuarios.dto.UsuarioRequestDTO;
import com.example.usuarios.dto.UsuarioResponseDTO;
import com.example.usuarios.exception.RecursoNoEncontradoException;
import com.example.usuarios.model.Usuario;
import com.example.usuarios.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock UsuarioRepository repository;
    @InjectMocks UsuarioService service;

    private UsuarioRequestDTO dtoBase() {
        UsuarioRequestDTO dto = new UsuarioRequestDTO();
        dto.setNombre("Juan");
        dto.setApellido("Perez");
        dto.setEmail("juan@test.com");
        dto.setPassword("123456");
        dto.setRol("ADMIN");
        return dto;
    }

    private Usuario usuarioMock(Long id, String email) {
        Usuario u = new Usuario();
        u.setId(id);
        u.setNombre("Juan");
        u.setApellido("Perez");
        u.setEmail(email);
        u.setPassword("123456");
        u.setRol("ADMIN");
        u.setActivo(true);
        u.setFechaCreacion(LocalDateTime.now());
        return u;
    }

    @Test
    void crear_exito() {
        when(repository.existsByEmail("juan@test.com")).thenReturn(false);
        when(repository.save(any())).thenReturn(usuarioMock(100L, "juan@test.com"));

        UsuarioResponseDTO resp = service.crear(dtoBase());

        assertThat(resp.getId()).isEqualTo(100L);
        assertThat(resp.getEmail()).isEqualTo("juan@test.com");
    }

    @Test
    void crear_emailDuplicado_lanzaExcepcion() {
        when(repository.existsByEmail("juan@test.com")).thenReturn(true);

        assertThatThrownBy(() -> service.crear(dtoBase()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Ya existe un usuario con el email");
    }

    @Test
    void actualizar_exito() {
        Usuario existente = usuarioMock(1L, "juan@test.com");
        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(any())).thenReturn(existente);

        UsuarioResponseDTO resp = service.actualizar(1L, dtoBase());

        assertThat(resp.getId()).isEqualTo(1L);
        verify(repository).save(any());
    }

    @Test
    void actualizar_cambiaEmailDuplicado_lanzaExcepcion() {
        Usuario existente = usuarioMock(1L, "otro@test.com");
        UsuarioRequestDTO dto = dtoBase();
        dto.setEmail("nuevo@test.com");

        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.existsByEmail("nuevo@test.com")).thenReturn(true);

        assertThatThrownBy(() -> service.actualizar(1L, dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Ya existe un usuario con el email");
    }

    @Test
    void actualizar_mismoEmail_noValidaDuplicado() {
        Usuario existente = usuarioMock(1L, "juan@test.com");
        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(any())).thenReturn(existente);

        UsuarioResponseDTO resp = service.actualizar(1L, dtoBase());

        assertThat(resp.getEmail()).isEqualTo("juan@test.com");
    }

    @Test
    void actualizar_noEncontrado_lanzaExcepcion() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.actualizar(99L, dtoBase()))
                .isInstanceOf(RecursoNoEncontradoException.class)
                .hasMessageContaining("Usuario no encontrado");
    }

    @Test
    void eliminar_exito() {
        Usuario usuario = usuarioMock(1L, "juan@test.com");
        when(repository.findById(1L)).thenReturn(Optional.of(usuario));
        when(repository.save(any())).thenReturn(usuario);

        service.eliminar(1L);

        assertThat(usuario.getActivo()).isFalse();
        verify(repository).save(usuario);
    }

    @Test
    void eliminar_noEncontrado_lanzaExcepcion() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.eliminar(99L))
                .isInstanceOf(RecursoNoEncontradoException.class)
                .hasMessageContaining("Usuario no encontrado");
    }

    @Test
    void listarTodos_retornaLista() {
        when(repository.findByActivoTrue()).thenReturn(List.of(
                usuarioMock(1L, "juan@test.com"),
                usuarioMock(2L, "maria@test.com")
        ));

        assertThat(service.listarTodos()).hasSize(2);
    }

    @Test
    void obtenerPorId_encontrado() {
        when(repository.findById(1L)).thenReturn(Optional.of(usuarioMock(1L, "juan@test.com")));

        UsuarioResponseDTO resp = service.obtenerPorId(1L);

        assertThat(resp.getId()).isEqualTo(1L);
    }

    @Test
    void obtenerPorId_noEncontrado_lanzaExcepcion() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.obtenerPorId(99L))
                .isInstanceOf(RecursoNoEncontradoException.class)
                .hasMessageContaining("Usuario no encontrado");
    }

    @Test
    void obtenerPorRol_retornaLista() {
        when(repository.findByRolAndActivoTrue("ADMIN")).thenReturn(List.of(usuarioMock(1L, "juan@test.com")));

        assertThat(service.obtenerPorRol("ADMIN")).hasSize(1);
    }
}