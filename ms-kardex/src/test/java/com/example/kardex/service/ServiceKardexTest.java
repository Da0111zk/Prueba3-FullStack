package com.example.kardex.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.kardex.dto.MovimientoResponseDTO;
import com.example.kardex.dto.StockResponseDTO;
import com.example.kardex.model.MovimientoKardex;
import com.example.kardex.model.StockActual;
import com.example.kardex.repository.MovimientoKardexRepository;
import com.example.kardex.repository.StockActualRepository;

@ExtendWith(MockitoExtension.class)
public class ServiceKardexTest {

    // 1. Declaramos los DOS repositorios simulados (Mocks) que usa el servicio
    @Mock
    private MovimientoKardexRepository movimientoRepository;

    @Mock
    private StockActualRepository stockRepository;

    // 2. Inyectamos ambos Mocks automáticamente en el servicio real
    @InjectMocks
    private KardexService kardexService;

    @Test
    public void testObtenerTodosLosMovimientos_DebeRetornarListaDeDtos() {
        // ARRANGE
        List<MovimientoKardex> listaSimulada = new ArrayList<>();
        listaSimulada.add(new MovimientoKardex(1L, 10L, 20L, "INGRESO", 5, "POSITIVO", 10, 15, LocalDate.now(), "Compra inicial"));
        
        when(movimientoRepository.findAll()).thenReturn(listaSimulada);

        // ACT
        List<MovimientoResponseDTO> resultado = kardexService.obtenerTodosLosMovimientos();

        // ASSERT
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("INGRESO", resultado.get(0).getTipoMovimiento());
        assertEquals(15, resultado.get(0).getStockNuevo());
        
        verify(movimientoRepository, times(1)).findAll();
    }

    @Test
    public void testObtenerStock_CuandoExiste_DebeRetornarDto() {
        // ARRANGE
        StockActual stockSimulado = new StockActual(1L, 10L, 20L, 50);
        
        // Aquí interactuamos con el segundo repositorio simulado
        when(stockRepository.findByProductoIdAndBodegaId(10L, 20L)).thenReturn(Optional.of(stockSimulado));

        // ACT
        StockResponseDTO resultado = kardexService.obtenerStock(10L, 20L);

        // ASSERT
        assertNotNull(resultado);
        assertEquals(10L, resultado.getProductoId());
        assertEquals(20L, resultado.getBodegaId());
        assertEquals(50, resultado.getCantidad());
        
        verify(stockRepository, times(1)).findByProductoIdAndBodegaId(10L, 20L);
    }

    @Test
    public void testActualizarStockManual_CuandoExiste_DebeModificarYRetornarDto() {
        // ARRANGE
        StockActual stockOriginal = new StockActual(1L, 10L, 20L, 50);
        StockActual stockModificado = new StockActual(1L, 10L, 20L, 100);
        
        when(stockRepository.findByProductoIdAndBodegaId(10L, 20L)).thenReturn(Optional.of(stockOriginal));
        when(stockRepository.save(stockOriginal)).thenReturn(stockModificado);

        // ACT
        StockResponseDTO resultado = kardexService.actualizarStockManual(10L, 20L, 100);

        // ASSERT
        assertNotNull(resultado);
        assertEquals(100, resultado.getCantidad());
        
        verify(stockRepository, times(1)).findByProductoIdAndBodegaId(10L, 20L);
        verify(stockRepository, times(1)).save(stockOriginal);
    }
}