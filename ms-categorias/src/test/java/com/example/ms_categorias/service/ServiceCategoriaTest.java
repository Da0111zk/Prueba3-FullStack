package com.example.ms_categorias.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.ms_categorias.Dto.DtoResponseCategoria;
import com.example.ms_categorias.modelo.Categoria;
import com.example.ms_categorias.repository.CategoriaRepository;

@ExtendWith(MockitoExtension.class)
public class ServiceCategoriaTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private ServicioCategoria servicioCategoria;

    @Test
    public void testObtenerCategorias_DebeRetornarListaDeDtos() {
        // ARRANGE
        List<Categoria> listaSimulada = new ArrayList<>();
        listaSimulada.add(new Categoria(1L, "Electrodomestico", "lavadora", "reposicion"));
        
        when(categoriaRepository.findAll()).thenReturn(listaSimulada);

        //variable + el método )
        List<DtoResponseCategoria> resultado = servicioCategoria.obtenerCategorias();

        
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Electrodomestico", resultado.get(0).getNombre()); 
        
        verify(categoriaRepository, times(1)).findAll();
    }

    @Test
    public void testObtenerPorId_CuandoExiste_DebeRetornarDto() {
        // ARRANGE
        Categoria categoriaFalsa = new Categoria(1L, "Electrodomestico", "lavadora", "reposicion");
        
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoriaFalsa));

        // ACT
        Optional<DtoResponseCategoria> resultado = servicioCategoria.obtenerPorId(1L);

        // ASSERT
        assertTrue(resultado.isPresent());
        assertEquals("Electrodomestico", resultado.get().getNombre());
        
        verify(categoriaRepository, times(1)).findById(1L);
    }

    @Test
    public void testObtenerPorId_CuandoNoExiste_DebeRetornarOptionalVacio() {
        // ARRANGE
        when(categoriaRepository.findById(99L)).thenReturn(Optional.empty());

        // ACT
        Optional<DtoResponseCategoria> resultado = servicioCategoria.obtenerPorId(99L);

        // ASSERT
        assertTrue(resultado.isEmpty());
        
        verify(categoriaRepository, times(1)).findById(99L);
    }
}