package com.example.kardex.service;

import com.example.kardex.dto.MovimientoRequestDTO;
import com.example.kardex.dto.MovimientoResponseDTO;
import com.example.kardex.dto.StockResponseDTO;
import com.example.kardex.model.MovimientoKardex;
import com.example.kardex.model.StockActual;
import com.example.kardex.repository.MovimientoKardexRepository;
import com.example.kardex.repository.StockActualRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KardexServiceTest {

    @Mock
    private MovimientoKardexRepository movimientoRepository;

    @Mock
    private StockActualRepository stockRepository;

    @InjectMocks
    private KardexService kardexService;

    private MovimientoRequestDTO requestDTO;
    private StockActual stockActual;
    private MovimientoKardex movimientoKardex;

    @BeforeEach
    void setUp() {
        requestDTO = new MovimientoRequestDTO();
        requestDTO.setProductoId(1L);
        requestDTO.setBodegaId(1L);
        requestDTO.setCantidad(10);
        requestDTO.setTipoMovimiento("INGRESO");
        requestDTO.setReferencia("Compra #123");

        stockActual = new StockActual();
        stockActual.setProductoId(1L);
        stockActual.setBodegaId(1L);
        stockActual.setCantidad(20);

        movimientoKardex = new MovimientoKardex();
        movimientoKardex.setId(1L);
        movimientoKardex.setProductoId(1L);
        movimientoKardex.setBodegaId(1L);
        movimientoKardex.setTipoMovimiento("INGRESO");
        movimientoKardex.setCantidad(10);
        movimientoKardex.setStockAnterior(20);
        movimientoKardex.setStockNuevo(30);
        movimientoKardex.setFechaMovimiento(LocalDateTime.now());
        movimientoKardex.setReferencia("Compra #123");
    }

    // ---------- registrarMovimiento ----------

    @Test
    void registrarMovimiento_ingreso_exitoso() {
        // Given: stock existente
        when(stockRepository.findByProductoIdAndBodegaId(1L, 1L))
                .thenReturn(Optional.of(stockActual));
        when(stockRepository.save(any(StockActual.class))).thenReturn(stockActual);
        when(movimientoRepository.save(any(MovimientoKardex.class))).thenReturn(movimientoKardex);

        // When
        MovimientoResponseDTO response = kardexService.registrarMovimiento(requestDTO);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.getProductoId());
        assertEquals(30, response.getStockNuevo());
        verify(stockRepository).save(stockActual);
        verify(movimientoRepository).save(any(MovimientoKardex.class));
    }

    @Test
    void registrarMovimiento_ingreso_creaStockSiNoExiste() {
        // Given: no existe stock
        when(stockRepository.findByProductoIdAndBodegaId(1L, 1L))
                .thenReturn(Optional.empty());
        when(stockRepository.save(any(StockActual.class))).thenReturn(stockActual);
        when(movimientoRepository.save(any(MovimientoKardex.class))).thenReturn(movimientoKardex);

        // When
        MovimientoResponseDTO response = kardexService.registrarMovimiento(requestDTO);

        // Then
        assertNotNull(response);
        verify(stockRepository).save(any(StockActual.class));
    }

    @Test
    void registrarMovimiento_egreso_exitoso() {
        // Given
        requestDTO.setTipoMovimiento("EGRESO");
        when(stockRepository.findByProductoIdAndBodegaId(1L, 1L))
                .thenReturn(Optional.of(stockActual));
        when(stockRepository.save(any(StockActual.class))).thenReturn(stockActual);
        when(movimientoRepository.save(any(MovimientoKardex.class))).thenReturn(movimientoKardex);

        // When
        MovimientoResponseDTO response = kardexService.registrarMovimiento(requestDTO);

        // Then
        assertNotNull(response);
        assertEquals(10, response.getStockNuevo()); // 20 - 10
        verify(stockRepository).save(stockActual);
    }

    @Test
    void registrarMovimiento_egreso_stockInsuficiente_lanzaExcepcion() {
        // Given: stock menor que cantidad a egresar
        stockActual.setCantidad(5);
        requestDTO.setTipoMovimiento("EGRESO");
        requestDTO.setCantidad(10);
        when(stockRepository.findByProductoIdAndBodegaId(1L, 1L))
                .thenReturn(Optional.of(stockActual));

        // When / Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> kardexService.registrarMovimiento(requestDTO));
        assertEquals("Stock insuficiente para realizar el egreso", exception.getMessage());
        verify(stockRepository, never()).save(any());
        verify(movimientoRepository, never()).save(any());
    }

    @Test
    void registrarMovimiento_ajustePositivo_exitoso() {
        // Given
        requestDTO.setTipoMovimiento("AJUSTE");
        requestDTO.setSigno("POSITIVO");
        when(stockRepository.findByProductoIdAndBodegaId(1L, 1L))
                .thenReturn(Optional.of(stockActual));
        when(stockRepository.save(any(StockActual.class))).thenReturn(stockActual);
        when(movimientoRepository.save(any(MovimientoKardex.class))).thenReturn(movimientoKardex);

        // When
        MovimientoResponseDTO response = kardexService.registrarMovimiento(requestDTO);

        // Then
        assertNotNull(response);
        assertEquals(30, response.getStockNuevo()); // 20 + 10
    }

    @Test
    void registrarMovimiento_ajusteNegativo_exitoso() {
        // Given
        requestDTO.setTipoMovimiento("AJUSTE");
        requestDTO.setSigno("NEGATIVO");
        when(stockRepository.findByProductoIdAndBodegaId(1L, 1L))
                .thenReturn(Optional.of(stockActual));
        when(stockRepository.save(any(StockActual.class))).thenReturn(stockActual);
        when(movimientoRepository.save(any(MovimientoKardex.class))).thenReturn(movimientoKardex);

        // When
        MovimientoResponseDTO response = kardexService.registrarMovimiento(requestDTO);

        // Then
        assertNotNull(response);
        assertEquals(10, response.getStockNuevo()); // 20 - 10
    }

    @Test
    void registrarMovimiento_ajusteNegativo_stockInsuficiente_lanzaExcepcion() {
        // Given
        stockActual.setCantidad(5);
        requestDTO.setTipoMovimiento("AJUSTE");
        requestDTO.setSigno("NEGATIVO");
        requestDTO.setCantidad(10);
        when(stockRepository.findByProductoIdAndBodegaId(1L, 1L))
                .thenReturn(Optional.of(stockActual));

        // When / Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> kardexService.registrarMovimiento(requestDTO));
        assertEquals("El ajuste dejaría stock negativo", exception.getMessage());
        verify(stockRepository, never()).save(any());
        verify(movimientoRepository, never()).save(any());
    }

    @Test
    void registrarMovimiento_tipoInvalido_lanzaExcepcion() {
        // Given
        requestDTO.setTipoMovimiento("INVALIDO");
        when(stockRepository.findByProductoIdAndBodegaId(1L, 1L))
                .thenReturn(Optional.of(stockActual));

        // When / Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> kardexService.registrarMovimiento(requestDTO));
        assertEquals("Tipo de movimiento inválido", exception.getMessage());
        verify(stockRepository, never()).save(any());
        verify(movimientoRepository, never()).save(any());
    }

    // ---------- obtenerStock ----------

    @Test
    void obtenerStock_exitoso() {
        // Given
        when(stockRepository.findByProductoIdAndBodegaId(1L, 1L))
                .thenReturn(Optional.of(stockActual));

        // When
        StockResponseDTO response = kardexService.obtenerStock(1L, 1L);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.getProductoId());
        assertEquals(20, response.getCantidad());
    }

    @Test
    void obtenerStock_noEncontrado_lanzaExcepcion() {
        // Given
        when(stockRepository.findByProductoIdAndBodegaId(1L, 1L))
                .thenReturn(Optional.empty());

        // When / Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> kardexService.obtenerStock(1L, 1L));
        assertEquals("No existe stock para ese producto y bodega", exception.getMessage());
    }

    // ---------- listarMovimientos ----------

    @Test
    void listarMovimientos_exitoso_conDatos() {
        // Given
        when(movimientoRepository.findAll()).thenReturn(List.of(movimientoKardex));

        // When
        List<MovimientoResponseDTO> result = kardexService.listarMovimientos();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
    }

    @Test
    void listarMovimientos_vacio() {
        // Given
        when(movimientoRepository.findAll()).thenReturn(List.of());

        // When
        List<MovimientoResponseDTO> result = kardexService.listarMovimientos();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}