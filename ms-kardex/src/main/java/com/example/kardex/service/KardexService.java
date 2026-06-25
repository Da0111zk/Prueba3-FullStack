package com.example.kardex.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.kardex.dto.MovimientoRequestDTO;
import com.example.kardex.dto.MovimientoResponseDTO;
import com.example.kardex.dto.StockResponseDTO;
import com.example.kardex.model.MovimientoKardex;
import com.example.kardex.model.StockActual;
import com.example.kardex.repository.MovimientoKardexRepository;
import com.example.kardex.repository.StockActualRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor // Genera el constructor para las variables 'final' automáticamente
public class KardexService {

    private final MovimientoKardexRepository movimientoRepository;
    private final StockActualRepository stockRepository;

    /**
     * REGISTRAR MOVIMIENTO (INGRESO, EGRESO o AJUSTE)
     */
    @Transactional
    public MovimientoResponseDTO registrarMovimiento(MovimientoRequestDTO dto) {
        StockActual stock = stockRepository.findByProductoIdAndBodegaId(dto.getProductoId(), dto.getBodegaId())
                .orElseGet(() -> {
                    StockActual nuevo = new StockActual();
                    nuevo.setProductoId(dto.getProductoId());
                    nuevo.setBodegaId(dto.getBodegaId());
                    nuevo.setCantidad(0);
                    return stockRepository.save(nuevo);
                });

        int stockAnterior = stock.getCantidad();
        int stockNuevo;

        switch (dto.getTipoMovimiento()) {
            case "INGRESO" -> stockNuevo = stockAnterior + dto.getCantidad();
            case "EGRESO" -> {
                stockNuevo = stockAnterior - dto.getCantidad();
                if (stockNuevo < 0) {
                    throw new RuntimeException("Stock insuficiente para realizar el egreso");
                }
            }
            case "AJUSTE" -> {
                String signo = dto.getSigno() == null ? "POSITIVO" : dto.getSigno();
                stockNuevo = "NEGATIVO".equals(signo)
                        ? stockAnterior - dto.getCantidad()
                        : stockAnterior + dto.getCantidad();

                if (stockNuevo < 0) {
                    throw new RuntimeException("El ajuste dejaría stock negativo");
                }
            }
            default -> throw new RuntimeException("Tipo de movimiento inválido");
        }

        stock.setCantidad(stockNuevo);
        stockRepository.save(stock);

        MovimientoKardex movimiento = new MovimientoKardex();
        movimiento.setProductoId(dto.getProductoId());
        movimiento.setBodegaId(dto.getBodegaId());
        movimiento.setTipoMovimiento(dto.getTipoMovimiento());
        movimiento.setCantidad(dto.getCantidad());
        movimiento.setSigno(dto.getSigno());
        movimiento.setStockAnterior(stockAnterior);
        movimiento.setStockNuevo(stockNuevo);
        movimiento.setFechaMovimiento(LocalDate.now());
        movimiento.setReferencia(dto.getReferencia());

        movimiento = movimientoRepository.save(movimiento);

        MovimientoResponseDTO response = new MovimientoResponseDTO();
        response.setId(movimiento.getId());
        response.setProductoId(movimiento.getProductoId());
        response.setBodegaId(movimiento.getBodegaId());
        response.setTipoMovimiento(movimiento.getTipoMovimiento());
        response.setCantidad(movimiento.getCantidad());
        response.setSigno(movimiento.getSigno());
        response.setStockAnterior(movimiento.getStockAnterior());
        response.setStockNuevo(movimiento.getStockNuevo());
        response.setFechaMovimiento(movimiento.getFechaMovimiento());
        response.setReferencia(movimiento.getReferencia());

        return response;
    }

    /**
     * OBTENER STOCK ACTUAL DE UN PRODUCTO EN UNA BODEGA SPECÍFICA
     */
    @Transactional(readOnly = true)
    public StockResponseDTO obtenerStock(Long productoId, Long bodegaId) {
        StockActual stock = stockRepository.findByProductoIdAndBodegaId(productoId, bodegaId)
                .orElseThrow(() -> new RuntimeException("No existe stock para ese producto y bodega"));

        StockResponseDTO dto = new StockResponseDTO();
        dto.setProductoId(stock.getProductoId());
        dto.setBodegaId(stock.getBodegaId());
        dto.setCantidad(stock.getCantidad());
        return dto;
    }

    /**
     * OBTENER HISTORIAL COMPLETO DE MOVIMIENTOS
     */
    @Transactional(readOnly = true)
    public List<MovimientoResponseDTO> obtenerTodosLosMovimientos() {
        return movimientoRepository.findAll().stream().map(movimiento -> {
            MovimientoResponseDTO response = new MovimientoResponseDTO();
            response.setId(movimiento.getId());
            response.setProductoId(movimiento.getProductoId());
            response.setBodegaId(movimiento.getBodegaId());
            response.setTipoMovimiento(movimiento.getTipoMovimiento());
            response.setCantidad(movimiento.getCantidad());
            response.setSigno(movimiento.getSigno());
            response.setStockAnterior(movimiento.getStockAnterior());
            response.setStockNuevo(movimiento.getStockNuevo());
            response.setFechaMovimiento(movimiento.getFechaMovimiento());
            response.setReferencia(movimiento.getReferencia());
            return response;
        }).toList();
    }

    /**
     * ACTUALIZAR CANTIDAD DE STOCK DE FORMA MANUAL (PUT)
     */
    @Transactional
    public StockResponseDTO actualizarStockManual(Long productoId, Long bodegaId, Integer nuevaCantidad) {
        StockActual stock = stockRepository.findByProductoIdAndBodegaId(productoId, bodegaId)
                .orElseThrow(() -> new RuntimeException("No existe registro de stock para este producto y bodega"));
        
        stock.setCantidad(nuevaCantidad);
        stock = stockRepository.save(stock);
        
        StockResponseDTO dto = new StockResponseDTO();
        dto.setProductoId(stock.getProductoId());
        dto.setBodegaId(stock.getBodegaId());
        dto.setCantidad(stock.getCantidad());
        return dto;
    }

    /**
     * ELIMINAR FÍSICAMENTE UN REGISTRO DE STOCK (DELETE)
     */
    @Transactional
    public void eliminarStock(Long productoId, Long bodegaId) {
        StockActual stock = stockRepository.findByProductoIdAndBodegaId(productoId, bodegaId)
                .orElseThrow(() -> new RuntimeException("No existe registro de stock para eliminar"));
        stockRepository.delete(stock);
    }
}