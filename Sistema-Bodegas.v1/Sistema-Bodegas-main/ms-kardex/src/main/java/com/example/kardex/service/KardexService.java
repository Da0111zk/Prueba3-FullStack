package com.example.kardex.service;

import com.example.kardex.dto.MovimientoRequestDTO;
import com.example.kardex.dto.MovimientoResponseDTO;
import com.example.kardex.dto.StockResponseDTO;
import com.example.kardex.model.MovimientoKardex;
import com.example.kardex.model.StockActual;
import com.example.kardex.repository.MovimientoKardexRepository;
import com.example.kardex.repository.StockActualRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class KardexService {

    private final MovimientoKardexRepository movimientoRepository;
    private final StockActualRepository stockRepository;

    @Transactional(readOnly = true)
    public List<MovimientoResponseDTO> listarMovimientos() {
        return movimientoRepository.findAll()
                .stream()
                .map(this::toMovimientoResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public MovimientoResponseDTO obtenerMovimientoPorId(Long id) {
        MovimientoKardex movimiento = movimientoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No se encontró el movimiento con id: " + id
                ));

        return toMovimientoResponseDTO(movimiento);
    }

    @Transactional
    public MovimientoResponseDTO registrarMovimiento(MovimientoRequestDTO dto) {
        validarMovimiento(dto);

        StockActual stock = stockRepository.findByProductoIdAndBodegaId(dto.getProductoId(), dto.getBodegaId())
                .orElseGet(() -> {
                    StockActual nuevo = new StockActual();
                    nuevo.setProductoId(dto.getProductoId());
                    nuevo.setBodegaId(dto.getBodegaId());
                    nuevo.setCantidad(0);
                    return stockRepository.save(nuevo);
                });

        int stockAnterior = stock.getCantidad();
        int stockNuevo = calcularStockNuevo(stockAnterior, dto);

        stock.setCantidad(stockNuevo);
        stockRepository.save(stock);

        MovimientoKardex movimiento = new MovimientoKardex();
        movimiento.setProductoId(dto.getProductoId());
        movimiento.setBodegaId(dto.getBodegaId());
        movimiento.setTipoMovimiento(normalizar(dto.getTipoMovimiento()));
        movimiento.setCantidad(dto.getCantidad());
        movimiento.setSigno(dto.getSigno() != null ? normalizar(dto.getSigno()) : null);
        movimiento.setStockAnterior(stockAnterior);
        movimiento.setStockNuevo(stockNuevo);
        movimiento.setFechaMovimiento(LocalDateTime.now());
        movimiento.setReferencia(dto.getReferencia());

        MovimientoKardex guardado = movimientoRepository.save(movimiento);

        log.info("Movimiento registrado. Id={}, productoId={}, bodegaId={}, tipo={}, cantidad={}",
                guardado.getId(),
                guardado.getProductoId(),
                guardado.getBodegaId(),
                guardado.getTipoMovimiento(),
                guardado.getCantidad());

        return toMovimientoResponseDTO(guardado);
    }

    @Transactional
    public MovimientoResponseDTO actualizarMovimiento(Long id, MovimientoRequestDTO dto) {
        validarMovimiento(dto);

        MovimientoKardex movimientoExistente = movimientoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No se encontró el movimiento con id: " + id
                ));

        movimientoExistente.setProductoId(dto.getProductoId());
        movimientoExistente.setBodegaId(dto.getBodegaId());
        movimientoExistente.setTipoMovimiento(normalizar(dto.getTipoMovimiento()));
        movimientoExistente.setCantidad(dto.getCantidad());
        movimientoExistente.setSigno(dto.getSigno() != null ? normalizar(dto.getSigno()) : null);
        movimientoExistente.setReferencia(dto.getReferencia());



        MovimientoKardex actualizado = movimientoRepository.save(movimientoExistente);

        log.info("Movimiento actualizado. Id={}", actualizado.getId());

        return toMovimientoResponseDTO(actualizado);
    }

    @Transactional
    public void eliminarMovimiento(Long id) {
        MovimientoKardex movimiento = movimientoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No se encontró el movimiento con id: " + id
                ));
        movimientoRepository.delete(movimiento);

        log.info("Movimiento eliminado. Id={}", id);
    }

    @Transactional(readOnly = true)
    public StockResponseDTO obtenerStock(Long productoId, Long bodegaId) {
        StockActual stock = stockRepository.findByProductoIdAndBodegaId(productoId, bodegaId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No existe stock para el producto " + productoId + " en la bodega " + bodegaId
                ));

        StockResponseDTO dto = new StockResponseDTO();
        dto.setProductoId(stock.getProductoId());
        dto.setBodegaId(stock.getBodegaId());
        dto.setCantidad(stock.getCantidad());
        return dto;
    }

    private int calcularStockNuevo(int stockAnterior, MovimientoRequestDTO dto) {
        String tipoMovimiento = normalizar(dto.getTipoMovimiento());

        return switch (tipoMovimiento) {
            case "INGRESO" -> stockAnterior + dto.getCantidad();

            case "EGRESO" -> {
                int stockNuevo = stockAnterior - dto.getCantidad();
                if (stockNuevo < 0) {
                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Stock insuficiente para realizar el egreso"
                    );
                }
                yield stockNuevo;
            }

            case "AJUSTE" -> {
                String signo = dto.getSigno() == null ? "POSITIVO" : normalizar(dto.getSigno());
                int stockNuevo = "NEGATIVO".equals(signo)
                        ? stockAnterior - dto.getCantidad()
                        : stockAnterior + dto.getCantidad();

                if (stockNuevo < 0) {
                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "El ajuste dejaría stock negativo"
                    );
                }
                yield stockNuevo;
            }

            default -> throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Tipo de movimiento inválido. Use INGRESO, EGRESO o AJUSTE"
            );
        };
    }

    private void validarMovimiento(MovimientoRequestDTO dto) {
        if (dto.getProductoId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El productoId es obligatorio");
        }

        if (dto.getBodegaId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El bodegaId es obligatorio");
        }

        if (dto.getCantidad() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La cantidad debe ser mayor que 0");
        }

        if (dto.getTipoMovimiento() == null || dto.getTipoMovimiento().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El tipoMovimiento es obligatorio");
        }

        String tipo = normalizar(dto.getTipoMovimiento());

        if (!tipo.equals("INGRESO") && !tipo.equals("EGRESO") && !tipo.equals("AJUSTE")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Tipo de movimiento inválido. Use INGRESO, EGRESO o AJUSTE"
            );
        }

        if (tipo.equals("AJUSTE") && dto.getSigno() != null) {
            String signo = normalizar(dto.getSigno());
            if (!signo.equals("POSITIVO") && !signo.equals("NEGATIVO")) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Signo inválido. Use POSITIVO o NEGATIVO"
                );
            }
        }
    }

    private MovimientoResponseDTO toMovimientoResponseDTO(MovimientoKardex movimiento) {
        MovimientoResponseDTO dto = new MovimientoResponseDTO();
        dto.setId(movimiento.getId());
        dto.setProductoId(movimiento.getProductoId());
        dto.setBodegaId(movimiento.getBodegaId());
        dto.setTipoMovimiento(movimiento.getTipoMovimiento());
        dto.setCantidad(movimiento.getCantidad());
        dto.setSigno(movimiento.getSigno());
        dto.setStockAnterior(movimiento.getStockAnterior());
        dto.setStockNuevo(movimiento.getStockNuevo());
        dto.setFechaMovimiento(movimiento.getFechaMovimiento());
        dto.setReferencia(movimiento.getReferencia());
        return dto;
    }

    private String normalizar(String valor) {
        return valor == null ? null : valor.trim().toUpperCase();
    }
}