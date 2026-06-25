package com.example.ms_ubicaciones.Service;

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

import com.example.ms_ubicaciones.dto.DtoResponseUbi;
import com.example.ms_ubicaciones.modelo.Ubicacion;
import com.example.ms_ubicaciones.repository.UbicacionRepository;
import com.example.ms_ubicaciones.service.UbicacionService;

@ExtendWith(MockitoExtension.class)
public class UbicacionServiceTest {

    @Mock
    private UbicacionRepository ubicacionRepository;

    @InjectMocks
    private UbicacionService ubicacionService;

    @Test
    public void testObtenerUbicaciones_DebeRetornarListaDeDtos() {
        // ARRANGE
        List<Ubicacion> listaSimulada = new ArrayList<>();
        listaSimulada.add(new Ubicacion(1L, "Bodega Central", "Pasillo 3", 987654321, 12345678, "ACTIVO", "contacto@bodega.cl"));
        
        when(ubicacionRepository.findAll()).thenReturn(listaSimulada);

        // ACT
        List<DtoResponseUbi> resultado = ubicacionService.obtenerUbicaciones();

        // ASSERT
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Bodega Central", resultado.get(0).getRazonSocial());
        
        verify(ubicacionRepository, times(1)).findAll();
    }

    @Test
    public void testObtenerPorId_CuandoExiste_DebeRetornarDto() {
        // ARRANGE
        Ubicacion ubicacionFalsa = new Ubicacion(1L, "Bodega Norte", "Pasillo 1", 955555555, 87654321, "ACTIVO", "norte@bodega.cl");
        
        when(ubicacionRepository.findById(1L)).thenReturn(Optional.of(ubicacionFalsa));

        // ACT
        Optional<DtoResponseUbi> resultado = ubicacionService.obtenerPorId(1L);

        // ASSERT
        assertTrue(resultado.isPresent());
        assertEquals("Bodega Norte", resultado.get().getRazonSocial());
        
        verify(ubicacionRepository, times(1)).findById(1L);
    }

    @Test
    public void testObtenerPorId_CuandoNoExiste_DebeRetornarOptionalVacio() {
        // ARRANGE
        when(ubicacionRepository.findById(99L)).thenReturn(Optional.empty());

        // ACT
        Optional<DtoResponseUbi> resultado = ubicacionService.obtenerPorId(99L);

        // ASSERT
        assertTrue(resultado.isEmpty());
        
        verify(ubicacionRepository, times(1)).findById(99L);
    }
}