package com.example.ubicaciones.service;

import com.example.ubicaciones.modelo.Ubicacion;
import com.example.ubicaciones.repository.UbicacionRepository;
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
class UbicacionServiceTest {

    @Mock
    private UbicacionRepository ubicacionRepository;

    @InjectMocks
    private UbicacionService ubicacionService;

    private Ubicacion ubicacion;

    @BeforeEach
    void setUp() {
        ubicacion = new Ubicacion();
        ubicacion.setUbicacionId(1L);
        ubicacion.setRazonSocial("Bodega Central");
        ubicacion.setPasillo("A-1");
        ubicacion.setTelefono(123456789);
        ubicacion.setRut("12.345.678-9");
        ubicacion.setActivo("ACTIVO");
        ubicacion.setEmail("contacto@bodega.cl");
    }


    @Test
    void obtenerUbicaciones_exitoso_conDatos() {

        when(ubicacionRepository.findAll()).thenReturn(List.of(ubicacion));


        List<Ubicacion> resultado = ubicacionService.obtenerUbicaciones();


        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Bodega Central", resultado.get(0).getRazonSocial());
        verify(ubicacionRepository, times(1)).findAll();
    }

    @Test
    void obtenerUbicaciones_vacio() {

        when(ubicacionRepository.findAll()).thenReturn(List.of());


        List<Ubicacion> resultado = ubicacionService.obtenerUbicaciones();


        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(ubicacionRepository, times(1)).findAll();
    }


    @Test
    void obtenerPorId_exitoso() {
        when(ubicacionRepository.findById(1L)).thenReturn(Optional.of(ubicacion));

        Optional<Ubicacion> resultado = ubicacionService.obtenerPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getUbicacionId());
        assertEquals("Bodega Central", resultado.get().getRazonSocial());
        verify(ubicacionRepository, times(1)).findById(1L);
    }

    @Test
    void obtenerPorId_noEncontrado() {
        when(ubicacionRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Ubicacion> resultado = ubicacionService.obtenerPorId(99L);

        assertFalse(resultado.isPresent());
        verify(ubicacionRepository, times(1)).findById(99L);
    }


    @Test
    void guardar_ubicacionNueva_exitoso() {
        Ubicacion nuevaUbicacion = new Ubicacion();
        nuevaUbicacion.setRazonSocial("Bodega Norte");
        nuevaUbicacion.setPasillo("B-2");
        nuevaUbicacion.setTelefono(987654321);
        nuevaUbicacion.setRut("98.765.432-1");
        nuevaUbicacion.setActivo("ACTIVO");
        nuevaUbicacion.setEmail("norte@bodega.cl");

        Ubicacion ubicacionGuardada = new Ubicacion();
        ubicacionGuardada.setUbicacionId(2L);
        ubicacionGuardada.setRazonSocial("Bodega Norte");
        ubicacionGuardada.setPasillo("B-2");
        ubicacionGuardada.setTelefono(987654321);
        ubicacionGuardada.setRut("98.765.432-1");
        ubicacionGuardada.setActivo("ACTIVO");
        ubicacionGuardada.setEmail("norte@bodega.cl");

        when(ubicacionRepository.save(any(Ubicacion.class))).thenReturn(ubicacionGuardada);

        Ubicacion resultado = ubicacionService.guardar(nuevaUbicacion);

        assertNotNull(resultado);
        assertEquals(2L, resultado.getUbicacionId());
        assertEquals("Bodega Norte", resultado.getRazonSocial());
        verify(ubicacionRepository, times(1)).save(any(Ubicacion.class));
    }

    @Test
    void guardar_ubicacionExistente_actualiza() {
        Ubicacion ubicacionExistente = new Ubicacion();
        ubicacionExistente.setUbicacionId(1L);
        ubicacionExistente.setRazonSocial("Bodega Central");
        ubicacionExistente.setPasillo("A-1");
        ubicacionExistente.setTelefono(123456789);
        ubicacionExistente.setRut("12.345.678-9");
        ubicacionExistente.setActivo("INACTIVO"); // Cambio de estado
        ubicacionExistente.setEmail("contacto@bodega.cl");

        when(ubicacionRepository.save(any(Ubicacion.class))).thenReturn(ubicacionExistente);

        Ubicacion resultado = ubicacionService.guardar(ubicacionExistente);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getUbicacionId());
        assertEquals("INACTIVO", resultado.getActivo());
        verify(ubicacionRepository, times(1)).save(any(Ubicacion.class));
    }


    @Test
    void eliminar_exitoso() {
        Long idEliminar = 1L;
        doNothing().when(ubicacionRepository).deleteById(idEliminar);

        ubicacionService.eliminar(idEliminar);

        verify(ubicacionRepository, times(1)).deleteById(idEliminar);
    }

    @Test
    void eliminar_ubicacionInexistente_noLanzaExcepcion() {
        Long idInexistente = 99L;
        doNothing().when(ubicacionRepository).deleteById(idInexistente);

        assertDoesNotThrow(() -> ubicacionService.eliminar(idInexistente));
        verify(ubicacionRepository, times(1)).deleteById(idInexistente);
    }
}