package com.example.ms_categorias.service;

import com.example.ms_categorias.modelo.Categoria;
import com.example.ms_categorias.repository.CategoriaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServicioCategoriaTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private ServicioCategoria servicioCategoria;

    private Categoria categoria;

    @BeforeEach
    void setUp() {
        // Crear una categoría de prueba usando constructor y setters
        categoria = new Categoria();
        categoria.setCategoriaId(1L);
        categoria.setNombre("Electrónicos");
        categoria.setDescripcion("Productos electrónicos y gadgets");
        categoria.setEstado("ACTIVO");
    }

    // ---------- obtenerCategorias ----------

    @Test
    void obtenerCategorias_exitoso_conDatos() {
        // Given
        when(categoriaRepository.findAll()).thenReturn(List.of(categoria));

        // When
        List<Categoria> resultado = servicioCategoria.obtenerCategorias();

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Electrónicos", resultado.get(0).getNombre());
        verify(categoriaRepository, times(1)).findAll();
    }

    @Test
    void obtenerCategorias_vacio() {
        // Given
        when(categoriaRepository.findAll()).thenReturn(List.of());

        // When
        List<Categoria> resultado = servicioCategoria.obtenerCategorias();

        // Then
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(categoriaRepository, times(1)).findAll();
    }

    // ---------- obtenerPorId ----------

    @Test
    void obtenerPorId_exitoso() {
        // Given
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));

        // When
        Optional<Categoria> resultado = servicioCategoria.obtenerPorId(1L);

        // Then
        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getCategoriaId());
        assertEquals("Electrónicos", resultado.get().getNombre());
        verify(categoriaRepository, times(1)).findById(1L);
    }

    @Test
    void obtenerPorId_noEncontrado() {
        // Given
        when(categoriaRepository.findById(99L)).thenReturn(Optional.empty());

        // When
        Optional<Categoria> resultado = servicioCategoria.obtenerPorId(99L);

        // Then
        assertFalse(resultado.isPresent());
        verify(categoriaRepository, times(1)).findById(99L);
    }

    // ---------- guardar ----------

    @Test
    void guardar_categoriaNueva_exitoso() {
        // Given
        Categoria nuevaCategoria = new Categoria();
        nuevaCategoria.setNombre("Hogar");
        nuevaCategoria.setDescripcion("Artículos para el hogar");
        nuevaCategoria.setEstado("ACTIVO");

        Categoria categoriaGuardada = new Categoria();
        categoriaGuardada.setCategoriaId(2L);
        categoriaGuardada.setNombre("Hogar");
        categoriaGuardada.setDescripcion("Artículos para el hogar");
        categoriaGuardada.setEstado("ACTIVO");

        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoriaGuardada);

        // When
        Categoria resultado = servicioCategoria.guardar(nuevaCategoria);

        // Then
        assertNotNull(resultado);
        assertEquals(2L, resultado.getCategoriaId());
        assertEquals("Hogar", resultado.getNombre());
        verify(categoriaRepository, times(1)).save(any(Categoria.class));
    }

    @Test
    void guardar_categoriaExistente_actualiza() {
        // Given
        Categoria categoriaExistente = new Categoria();
        categoriaExistente.setCategoriaId(1L);
        categoriaExistente.setNombre("Electrónicos");
        categoriaExistente.setDescripcion("Descripción actualizada");
        categoriaExistente.setEstado("ACTIVO");

        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoriaExistente);

        // When
        Categoria resultado = servicioCategoria.guardar(categoriaExistente);

        // Then
        assertNotNull(resultado);
        assertEquals(1L, resultado.getCategoriaId());
        assertEquals("Descripción actualizada", resultado.getDescripcion());
        verify(categoriaRepository, times(1)).save(any(Categoria.class));
    }

    // ---------- eliminar ----------

    @Test
    void eliminar_exitoso() {
        // Given
        Long idEliminar = 1L;
        doNothing().when(categoriaRepository).deleteById(idEliminar);

        // When
        servicioCategoria.eliminar(idEliminar);

        // Then
        verify(categoriaRepository, times(1)).deleteById(idEliminar);
    }

    @Test
    void eliminar_categoriaInexistente_noLanzaExcepcion() {
        // Given: deleteById no lanza excepción incluso si no existe
        Long idInexistente = 99L;
        doNothing().when(categoriaRepository).deleteById(idInexistente);

        // When / Then: no debe lanzar excepción
        assertDoesNotThrow(() -> servicioCategoria.eliminar(idInexistente));
        verify(categoriaRepository, times(1)).deleteById(idInexistente);
    }
}