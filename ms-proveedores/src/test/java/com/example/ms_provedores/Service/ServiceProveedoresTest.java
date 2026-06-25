package com.example.ms_provedores.Service;
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

import java.util.ArrayList;

import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;



import com.example.ms_proveedores.dto.DtoResponseProvedor;
import com.example.ms_proveedores.modelo.Proveedor;
import com.example.ms_proveedores.repository.ProvedorRepository;
import com.example.ms_proveedores.service.ProvedorService;


@ExtendWith(MockitoExtension.class)
public class ServiceProveedoresTest {
    @Mock
    private ProvedorRepository provedorRepository;

    @InjectMocks
    private ProvedorService provedorService;

    @Test
    public void testObtenerCategorias_DebeRetornarListaDeDtos() {
        // ARRANGE
        List<Proveedor> listaSimulada = new ArrayList<>();
        listaSimulada.add(new Proveedor(1L, "spa electrodomestios juan",987638, "juan@gmail.com", 54445345,"reposicion"));
        
        when(provedorRepository.findAll()).thenReturn(listaSimulada);

        //variable + el método )
        List<DtoResponseProvedor> resultado = provedorService.obtenerProvedores();

        
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Electrodomestico", resultado.get(0).getProvedorId()); 
        
        verify(provedorRepository, times(1)).findAll();
    }

    @Test
    public void testObtenerPorId_CuandoExiste_DebeRetornarDto() {
        // ARRANGE
        Proveedor proveedorFalsa = new Proveedor(1L, "spa electrodomestios juan",987638, "juan@gmail.com", 54445345,"reposicion");
        
        when(provedorRepository.findById(1L)).thenReturn(Optional.of(proveedorFalsa));

        // ACT
        Optional<DtoResponseProvedor> resultado = provedorService.obtenerPorId1(1L);

        // ASSERT
        assertTrue(resultado.isPresent());
        assertEquals("Electrodomestico", resultado.get().getProvedorId());
        
        verify(provedorRepository, times(1)).findById(1L);
    }

    @Test
    public void testObtenerPorId_CuandoNoExiste_DebeRetornarOptionalVacio() {
        // ARRANGE
        when(provedorRepository.findById(99L)).thenReturn(Optional.empty());

        // ACT
        Optional<DtoResponseProvedor> resultado = provedorService.obtenerPorId1(99L);

        // ASSERT
        assertTrue(resultado.isEmpty());
        
        verify(provedorRepository, times(1)).findById(99L);
    }


}
