package com.example.ms_producto.Service;

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

import com.example.ms_producto.dto.DtoResponseProducto;
import com.example.ms_producto.modelo.Productos;
import com.example.ms_producto.repository.ProductoRepository;
import com.example.ms_producto.service.ProductoService;

@ExtendWith(MockitoExtension.class)
public class ServiceProductoTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoServicio;

    @Test
    public void testObtenerCategorias_DebeRetornarListaDeDtos() {
        // ARRANGE - Corregido a List<Producto>
        List<Productos> listaSimulada = new ArrayList<>();
        listaSimulada.add(new Productos(1L, "Electrodomestico", "E123", 54445, "reposicion"));
        
        when(productoRepository.findAll()).thenReturn(listaSimulada);

        // ACT
        List<DtoResponseProducto> resultado = productoServicio.obtenerProductos();

        // ASSERT
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Electrodomestico", resultado.get(0).getNombre()); 
        
        verify(productoRepository, times(1)).findAll();
    }

    @Test
    public void testObtenerPorId_CuandoExiste_DebeRetornarDto() {
        // ARRANGE - Corregido a Producto y new Producto
        Productos productoFalsa = new Productos(1L, "Electrodomestico", "E123", 54445, "reposicion");
        
        when(productoRepository.findById(1L)).thenReturn(Optional.of(productoFalsa));

        // ACT
        Optional<DtoResponseProducto> resultado = productoServicio.obtenerPorId(1L);

        // ASSERT
        assertTrue(resultado.isPresent());
        assertEquals("Electrodomestico", resultado.get().getNombre());
        
        verify(productoRepository, times(1)).findById(1L);
    }

    @Test
    public void testObtenerPorId_CuandoNoExiste_DebeRetornarOptionalVacio() {
        // ARRANGE
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        // ACT
        Optional<DtoResponseProducto> resultado = productoServicio.obtenerPorId(99L);

        // ASSERT
        assertTrue(resultado.isEmpty());
        
        verify(productoRepository, times(1)).findById(99L);
    }
}