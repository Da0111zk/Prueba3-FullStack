package com.example.ms_producto.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersUriSpec;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;

import com.example.ms_producto.modelo.Producto;
import com.example.ms_producto.repository.ProductoRepository;

import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private WebClient webClientCategorias;

    @InjectMocks
    private ProductoService productoService;

    @Mock
    @SuppressWarnings("rawtypes")
    private RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    @SuppressWarnings("rawtypes")
    private RequestHeadersSpec requestHeadersSpec;

    @Mock
    private ResponseSpec responseSpec;

    private Producto producto; 

    @BeforeEach
    void setUp() {
        producto = new Producto(
            1L, 
            "Mesa de Centro", 
            "SKU001", 
            new BigDecimal("199.99"), 
            "DISPONIBLE", 
            5L
        );
    }

    @Test
    void guardar_CuandoCategoriaExiste_DebeGuardarExitosamente() {
        when(webClientCategorias.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("/api/categorias/5")).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Object.class)).thenReturn(Mono.just(new Object()));

        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        Producto resultado = productoService.guardar(producto);

        assertNotNull(resultado);
        assertEquals("Mesa de Centro", resultado.getNombre());
        verify(productoRepository, times(1)).save(producto);
    }

    @Test
    void guardar_CuandoCategoriaNoExiste_DebeLanzarRuntimeException() {
        when(webClientCategorias.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("/api/categorias/5")).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Object.class)).thenReturn(Mono.error(new RuntimeException("404 Not Found")));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productoService.guardar(producto);
        });

        assertTrue(exception.getMessage().contains("Categoria con id 5 no existe"));
        verify(productoRepository, never()).save(any(Producto.class));
    }

    @Test
    void guardar_CuandoWebClientFallaPorTimeout_DebeLanzarRuntimeException() {
        when(webClientCategorias.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("/api/categorias/5")).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Object.class)).thenReturn(Mono.error(new RuntimeException("Timeout de conexion")));

        assertThrows(RuntimeException.class, () -> {
            productoService.guardar(producto);
        });
        verify(productoRepository, never()).save(any(Producto.class));
    }

    @Test
    void obtenerPorId_DebeRetornarProducto() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        Optional<Producto> resultado = productoService.obtenerPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getProductoId());
        assertEquals("Mesa de Centro", resultado.get().getNombre());
    }

    @Test
    void obtenerPorId_CuandoNoExiste_DebeRetornarOptionalVacio() {
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Producto> resultado = productoService.obtenerPorId(99L);

        assertFalse(resultado.isPresent());
    }

    @Test
    void obtenerPorId_ConIdNegativo_DebeRetornarOptionalVacio() {
        when(productoRepository.findById(-1L)).thenReturn(Optional.empty());

        Optional<Producto> resultado = productoService.obtenerPorId(-1L);

        assertFalse(resultado.isPresent());
    }

    @Test
    void obtenerPorId_ConIdNulo_DebeRetornarOptionalVacio() {
        when(productoRepository.findById(null)).thenReturn(Optional.empty());

        Optional<Producto> resultado = productoService.obtenerPorId(null);

        assertFalse(resultado.isPresent());
    }

    @Test
    void eliminar_DebeLlamarAlRepository() {
        productoService.eliminar(1L);

        verify(productoRepository, times(1)).deleteById(1L);
    }
}