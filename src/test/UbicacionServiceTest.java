package com.example.ms_ubicaciones.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

// Forzamos las importaciones exactas de tu microservicio de ubicaciones
import com.example.ms_ubicaciones.dto.DtoResponseUbi;
import com.example.ms_ubicaciones.modelo.Ubicacion;
import com.example.ms_ubicaciones.repository.UbicacionRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UbicacionServiceTest {

    @Mock
    private UbicacionRepository ubicacionRepository;

    @InjectMocks
    private UbicacionService ubicacionService;

    @Test
    public void testObtenerUbicaciones_DebeRetornarListaDeDtos() {
        List<Ubicacion> listaSimulada = new ArrayList<>();
        listaSimulada.add(new Ubicacion(1L, "Bodega Central", "Pasillo 3", 987654321, 12345678, "ACTIVO", "contacto@bodega.cl"));
        
        when(ubicacionRepository.findAll()).thenReturn(listaSimulada);

        List<DtoResponseUbi> resultado = ubicacionService.obtenerUbicaciones();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Bodega Central", resultado.get(0).getRazonSocial());
        
        verify(ubicacionRepository, times(1)).findAll();
    }

    @Test
    public void testObtenerPorId_CuandoExiste_DebeRetornarDto() {
        Ubicacion ubicacionFalsa = new Ubicacion(1L, "Bodega Norte", "Pasillo 1", 955555555, 87654321, "ACTIVO", "norte@bodega.cl");
        
        when(ubicacionRepository.findById(1L)).thenReturn(Optional.of(ubicacionFalsa));

        Optional<DtoResponseUbi> resultado = ubicacionService.obtenerPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Bodega Norte", resultado.get().getRazonSocial());
        
        verify(ubicacionRepository, times(1)).findById(1L);
    }

    @Test
    public void testObtenerPorId_CuandoNoExiste_DebeRetornarOptionalVacio() {
        when(ubicacionRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<DtoResponseUbi> resultado = ubicacionService.obtenerPorId(99L);

        assertTrue(resultado.isEmpty());
        
        verify(ubicacionRepository, times(1)).findById(99L);
    }
}