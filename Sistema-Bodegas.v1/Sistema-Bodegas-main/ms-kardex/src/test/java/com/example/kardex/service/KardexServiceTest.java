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
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

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

    private MovimientoKardex movimiento;
    private StockActual stock;
    private MovimientoRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        movimiento = new MovimientoKardex();
        movimiento.setId(1L);
        movimiento.setProductoId(100L);
        movimiento.setBodegaId(1L);
        movimiento.setTipoMovimiento("INGRESO");
        movimiento.setCantidad(10);
        movimiento.setSigno(null);
        movimiento.setStockAnterior(20);
        movimiento.setStockNuevo(30);
        movimiento.setFechaMovimiento(LocalDateTime.now());
        movimiento.setReferencia("Compra inicial");

        stock = new StockActual();
        stock.setId(1L);
        stock.setProductoId(100L);
        stock.setBodegaId(1L);
        stock.setCantidad(20);

        requestDTO = new MovimientoRequestDTO();
        requestDTO.setProductoId(100L);
        requestDTO.setBodegaId(1L);
        requestDTO.setTipoMovimiento("INGRESO");
        requestDTO.setCantidad(10);
        requestDTO.setReferencia("Compra inicial");
    }

    @Test
    void listarMovimientos_exitoso() {
        when(movimientoRepository.findAll()).thenReturn(List.of(movimiento));

        List<MovimientoResponseDTO> resultado = kardexService.listarMovimientos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(100L, resultado.get(0).getProductoId());
        assertEquals("INGRESO", resultado.get(0).getTipoMovimiento());
        verify(movimientoRepository, times(1)).findAll();
    }

    @Test
    void obtenerMovimientoPorId_exitoso() {
        when(movimientoRepository.findById(1L)).thenReturn(Optional.of(movimiento));

        MovimientoResponseDTO resultado = kardexService.obtenerMovimientoPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(100L, resultado.getProductoId());
        verify(movimientoRepository, times(1)).findById(1L);
    }

    @Test
    void obtenerMovimientoPorId_noEncontrado() {
        when(movimientoRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> kardexService.obtenerMovimientoPorId(99L));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertTrue(ex.getReason().contains("No se encontró el movimiento"));
        verify(movimientoRepository, times(1)).findById(99L);
    }

    @Test
    void registrarMovimiento_ingreso_exitoso() {
        when(stockRepository.findByProductoIdAndBodegaId(100L, 1L)).thenReturn(Optional.of(stock));
        when(stockRepository.save(any(StockActual.class))).thenReturn(stock);
        when(movimientoRepository.save(any(MovimientoKardex.class))).thenAnswer(invocation -> {
            MovimientoKardex m = invocation.getArgument(0);
            m.setId(1L);
            return m;
        });

        MovimientoResponseDTO resultado = kardexService.registrarMovimiento(requestDTO);

        assertNotNull(resultado);
        assertEquals(100L, resultado.getProductoId());
        assertEquals("INGRESO", resultado.getTipoMovimiento());
        assertEquals(20, resultado.getStockAnterior());
        assertEquals(30, resultado.getStockNuevo());

        verify(stockRepository, times(1)).findByProductoIdAndBodegaId(100L, 1L);
        verify(stockRepository, atLeastOnce()).save(any(StockActual.class));
        verify(movimientoRepository, times(1)).save(any(MovimientoKardex.class));
    }

    @Test
    void registrarMovimiento_egreso_conStockInsuficiente() {
        requestDTO.setTipoMovimiento("EGRESO");
        requestDTO.setCantidad(50);

        when(stockRepository.findByProductoIdAndBodegaId(100L, 1L)).thenReturn(Optional.of(stock));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> kardexService.registrarMovimiento(requestDTO));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("Stock insuficiente"));
        verify(stockRepository, times(1)).findByProductoIdAndBodegaId(100L, 1L);
        verify(movimientoRepository, never()).save(any(MovimientoKardex.class));
    }

    @Test
    void registrarMovimiento_ajusteNegativo_exitoso() {
        requestDTO.setTipoMovimiento("AJUSTE");
        requestDTO.setSigno("NEGATIVO");
        requestDTO.setCantidad(5);

        when(stockRepository.findByProductoIdAndBodegaId(100L, 1L)).thenReturn(Optional.of(stock));
        when(stockRepository.save(any(StockActual.class))).thenReturn(stock);
        when(movimientoRepository.save(any(MovimientoKardex.class))).thenAnswer(invocation -> {
            MovimientoKardex m = invocation.getArgument(0);
            m.setId(2L);
            return m;
        });

        MovimientoResponseDTO resultado = kardexService.registrarMovimiento(requestDTO);

        assertNotNull(resultado);
        assertEquals("AJUSTE", resultado.getTipoMovimiento());
        assertEquals("NEGATIVO", resultado.getSigno());
        assertEquals(20, resultado.getStockAnterior());
        assertEquals(15, resultado.getStockNuevo());
    }

    @Test
    void registrarMovimiento_tipoInvalido() {
        requestDTO.setTipoMovimiento("VENTA");

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> kardexService.registrarMovimiento(requestDTO));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("Tipo de movimiento inválido"));
        verify(movimientoRepository, never()).save(any(MovimientoKardex.class));
    }

    @Test
    void actualizarMovimiento_exitoso() {
        MovimientoRequestDTO updateDTO = new MovimientoRequestDTO();
        updateDTO.setProductoId(200L);
        updateDTO.setBodegaId(2L);
        updateDTO.setTipoMovimiento("AJUSTE");
        updateDTO.setCantidad(8);
        updateDTO.setSigno("POSITIVO");
        updateDTO.setReferencia("Corrección inventario");

        when(movimientoRepository.findById(1L)).thenReturn(Optional.of(movimiento));
        when(movimientoRepository.save(any(MovimientoKardex.class))).thenAnswer(invocation -> invocation.getArgument(0));

        MovimientoResponseDTO resultado = kardexService.actualizarMovimiento(1L, updateDTO);

        assertNotNull(resultado);
        assertEquals(200L, resultado.getProductoId());
        assertEquals(2L, resultado.getBodegaId());
        assertEquals("AJUSTE", resultado.getTipoMovimiento());
        assertEquals("POSITIVO", resultado.getSigno());
        assertEquals(8, resultado.getCantidad());

        verify(movimientoRepository, times(1)).findById(1L);
        verify(movimientoRepository, times(1)).save(any(MovimientoKardex.class));
    }

    @Test
    void actualizarMovimiento_noEncontrado() {
        when(movimientoRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> kardexService.actualizarMovimiento(99L, requestDTO));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertTrue(ex.getReason().contains("No se encontró el movimiento"));
        verify(movimientoRepository, times(1)).findById(99L);
        verify(movimientoRepository, never()).save(any(MovimientoKardex.class));
    }

    @Test
    void eliminarMovimiento_exitoso() {
        when(movimientoRepository.findById(1L)).thenReturn(Optional.of(movimiento));
        doNothing().when(movimientoRepository).delete(movimiento);

        assertDoesNotThrow(() -> kardexService.eliminarMovimiento(1L));

        verify(movimientoRepository, times(1)).findById(1L);
        verify(movimientoRepository, times(1)).delete(movimiento);
    }

    @Test
    void eliminarMovimiento_noEncontrado() {
        when(movimientoRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> kardexService.eliminarMovimiento(99L));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertTrue(ex.getReason().contains("No se encontró el movimiento"));
        verify(movimientoRepository, times(1)).findById(99L);
        verify(movimientoRepository, never()).delete(any(MovimientoKardex.class));
    }

    @Test
    void obtenerStock_exitoso() {
        when(stockRepository.findByProductoIdAndBodegaId(100L, 1L)).thenReturn(Optional.of(stock));

        StockResponseDTO resultado = kardexService.obtenerStock(100L, 1L);

        assertNotNull(resultado);
        assertEquals(100L, resultado.getProductoId());
        assertEquals(1L, resultado.getBodegaId());
        assertEquals(20, resultado.getCantidad());

        verify(stockRepository, times(1)).findByProductoIdAndBodegaId(100L, 1L);
    }

    @Test
    void obtenerStock_noEncontrado() {
        when(stockRepository.findByProductoIdAndBodegaId(100L, 1L)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> kardexService.obtenerStock(100L, 1L));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertTrue(ex.getReason().contains("No existe stock"));
        verify(stockRepository, times(1)).findByProductoIdAndBodegaId(100L, 1L);
    }
}