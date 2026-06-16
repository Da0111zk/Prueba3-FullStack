package com.example.ajustes.service;

import com.example.ajustes.dto.AjusteRequestDTO;
import com.example.ajustes.dto.AjusteResponseDTO;
import com.example.ajustes.model.Ajuste;
import com.example.ajustes.repository.AjusteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AjusteService {

    @Autowired
    private AjusteRepository repository;

    @Autowired
    private WebClient webClientProductos;

    @Autowired
    private WebClient webClientKardex;

    public AjusteResponseDTO crear(AjusteRequestDTO dto) {
        if (dto.getCantidadAjuste() == 0) {
            throw new RuntimeException("La cantidad de ajuste no puede ser cero");
        }

        validarExterno(
                webClientProductos,
                "/api/productos/" + dto.getProductoId(),
                "Producto " + dto.getProductoId() + " no existe en ms-productos"
        );

        Integer stockActual = obtenerStock(dto.getProductoId(), dto.getBodegaId());
        Integer stockNuevo = stockActual + dto.getCantidadAjuste();

        if (stockNuevo < 0) {
            throw new RuntimeException("El ajuste deja stock negativo");
        }

        Ajuste ajuste = new Ajuste();
        ajuste.setProductoId(dto.getProductoId());
        ajuste.setBodegaId(dto.getBodegaId());
        ajuste.setCantidadAjuste(dto.getCantidadAjuste());
        ajuste.setStockAnterior(stockActual);
        ajuste.setStockNuevo(stockNuevo);
        ajuste.setFechaAjuste(LocalDate.now());
        ajuste.setMotivo(dto.getMotivo());
        ajuste.setResponsable(dto.getResponsable());
        ajuste.setEstado("PENDIENTE");
        ajuste.setObservaciones(dto.getObservaciones());

        return toResponse(repository.save(ajuste));
    }

    public AjusteResponseDTO aprobar(Long id) {
        Ajuste ajuste = buscarEntidadPorId(id);

        if (!"PENDIENTE".equals(ajuste.getEstado())) {
            throw new RuntimeException("Solo se pueden aprobar ajustes en estado PENDIENTE");
        }

        try {
            Map<String, Object> movimiento = new HashMap<>();
            movimiento.put("productoId", ajuste.getProductoId());
            movimiento.put("bodegaId", ajuste.getBodegaId());
            movimiento.put("cantidad", Math.abs(ajuste.getCantidadAjuste()));
            movimiento.put("tipoMovimiento", "AJUSTE");
            movimiento.put("signo", ajuste.getCantidadAjuste() > 0 ? "POSITIVO" : "NEGATIVO");
            movimiento.put("referencia", "AJUSTE-" + ajuste.getId());

            webClientKardex.post()
                    .uri("/api/kardex/movimiento")
                    .bodyValue(movimiento)
                    .retrieve()
                    .bodyToMono(Object.class)
                    .timeout(Duration.ofSeconds(3))
                    .block();

            log.info("Movimiento AJUSTE registrado en Kardex para productoId={}", ajuste.getProductoId());
        } catch (Exception e) {
            log.warn("No se pudo registrar en Kardex: {}", e.getMessage());
            throw new RuntimeException("No se pudo registrar el movimiento en Kardex");
        }

        ajuste.setEstado("APROBADO");
        return toResponse(repository.save(ajuste));
    }

    public AjusteResponseDTO rechazar(Long id) {
        Ajuste ajuste = buscarEntidadPorId(id);

        if (!"PENDIENTE".equals(ajuste.getEstado())) {
            throw new RuntimeException("Solo se pueden rechazar ajustes en estado PENDIENTE");
        }

        ajuste.setEstado("RECHAZADO");
        return toResponse(repository.save(ajuste));
    }

    public List<AjusteResponseDTO> listarTodos() {
        return repository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
}

    public AjusteResponseDTO buscarPorId(Long id) {
        return toResponse(buscarEntidadPorId(id));
    }

    public List<AjusteResponseDTO> listarPendientes() {
        return repository.findByEstado("PENDIENTE").stream().map(this::toResponse).collect(Collectors.toList());
    }

    private Ajuste buscarEntidadPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ajuste no encontrado con ID: " + id));
    }

    private Integer obtenerStock(Long productoId, Long bodegaId) {
        try {
            Map<String, Object> stock = webClientKardex.get()
                    .uri("/api/kardex/stock/producto/" + productoId + "/bodega/" + bodegaId)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .timeout(Duration.ofSeconds(3))
                    .block();

            if (stock != null && stock.get("cantidad") != null) {
                return ((Number) stock.get("cantidad")).intValue();
            }
        } catch (Exception e) {
            log.warn("No se pudo obtener stock desde Kardex: {}", e.getMessage());
        }
        return 0;
    }

    private void validarExterno(WebClient client, String uri, String errorMsg) {
        try {
            client.get()
                    .uri(uri)
                    .retrieve()
                    .bodyToMono(Object.class)
                    .timeout(Duration.ofSeconds(3))
                    .block();
        } catch (Exception e) {
            throw new RuntimeException(errorMsg);
        }
    }

    private AjusteResponseDTO toResponse(Ajuste ajuste) {
        AjusteResponseDTO dto = new AjusteResponseDTO();
        dto.setId(ajuste.getId());
        dto.setProductoId(ajuste.getProductoId());
        dto.setBodegaId(ajuste.getBodegaId());
        dto.setCantidadAjuste(ajuste.getCantidadAjuste());
        dto.setStockAnterior(ajuste.getStockAnterior());
        dto.setStockNuevo(ajuste.getStockNuevo());
        dto.setFechaAjuste(ajuste.getFechaAjuste());
        dto.setMotivo(ajuste.getMotivo());
        dto.setResponsable(ajuste.getResponsable());
        dto.setEstado(ajuste.getEstado());
        dto.setObservaciones(ajuste.getObservaciones());
        return dto;
    }
}