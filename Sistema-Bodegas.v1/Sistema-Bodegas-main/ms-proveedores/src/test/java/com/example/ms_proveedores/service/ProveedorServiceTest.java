package com.example.ms_proveedores.service;

import com.example.ms_proveedores.modelo.Proveedor;
import com.example.ms_proveedores.repository.ProveedorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProveedorServiceTest {

    @Mock
    private ProveedorRepository proveedorRepository;

    @InjectMocks
    private ProveedorService proveedorService;

    @Test
    void obtenerProveedores_retornaLista() {
        Proveedor proveedor1 = new Proveedor();
        proveedor1.setProveedorId(1L);

        Proveedor proveedor2 = new Proveedor();
        proveedor2.setProveedorId(2L);

        when(proveedorRepository.findAll()).thenReturn(List.of(proveedor1, proveedor2));

        List<Proveedor> resultado = proveedorService.obtenerProveedores();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(proveedorRepository, times(1)).findAll();
    }

    @Test
    void obtenerPorId_retornaProveedor() {
        Proveedor proveedor = new Proveedor();
        proveedor.setProveedorId(1L);

        when(proveedorRepository.findById(1L)).thenReturn(Optional.of(proveedor));

        Optional<Proveedor> resultado = proveedorService.obtenerPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getProveedorId());
        verify(proveedorRepository, times(1)).findById(1L);
    }

    @Test
    void obtenerPorId_retornaVacio() {
        when(proveedorRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Proveedor> resultado = proveedorService.obtenerPorId(99L);

        assertTrue(resultado.isEmpty());
        verify(proveedorRepository, times(1)).findById(99L);
    }

    @Test
    void guardar_retornaProveedorGuardado() {
        Proveedor proveedor = new Proveedor();
        proveedor.setProveedorId(1L);

        when(proveedorRepository.save(any(Proveedor.class))).thenReturn(proveedor);

        Proveedor resultado = proveedorService.guardar(proveedor);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getProveedorId());
        verify(proveedorRepository, times(1)).save(proveedor);
    }

    @Test
    void eliminar_llamaDeleteById() {
        doNothing().when(proveedorRepository).deleteById(1L);

        assertDoesNotThrow(() -> proveedorService.eliminar(1L));

        verify(proveedorRepository, times(1)).deleteById(1L);
    }
}