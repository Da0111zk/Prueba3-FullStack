package com.example.traslados.service;

import com.example.traslados.dto.TrasladoRequestDTO;
import com.example.traslados.dto.TrasladoResponseDTO;
import com.example.traslados.exception.RecursoNoEncontradoException;
import com.example.traslados.model.Traslado;
import com.example.traslados.repository.TrasladoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Slf4j
@Service
public class TrasladoService {

    @Autowired
    private TrasladoRepository repository;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Value("${ms.productos.url}")
    private String productosUrl;

    @Value("${ms.ubicaciones.url}")
    private String ubicacionesUrl;

    public List<TrasladoResponseDTO> listarTodos() {
        log.info("Listando todos los traslados");
        return repository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public TrasladoResponseDTO obtenerPorId(Long id) {
        log.info("Buscando traslado con id {}", id);
        Traslado traslado = repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Traslado no encontrado con id: " + id));
        return toResponseDTO(traslado);
    }

    public List<TrasladoResponseDTO> obtenerPorProducto(Long productoId) {
        log.info("Buscando traslados por producto {}", productoId);
        return repository.findByProductoId(productoId)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public TrasladoResponseDTO crear(TrasladoRequestDTO dto) {
        validarReglas(dto);
        validarProductoExiste(dto.getProductoId());
        validarUbicacionExiste(dto.getUbicOrigen(), "origen");
        validarUbicacionExiste(dto.getUbicDestino(), "destino");

        Traslado traslado = new Traslado();
        traslado.setProductoId(dto.getProductoId());
        traslado.setUbicOrigen(dto.getUbicOrigen());
        traslado.setUbicDestino(dto.getUbicDestino());
        traslado.setCantidad(dto.getCantidad());
        traslado.setMotivo(dto.getMotivo());

        log.info("Guardando traslado del producto {} desde {} hacia {}",
                dto.getProductoId(), dto.getUbicOrigen(), dto.getUbicDestino());

        return toResponseDTO(repository.save(traslado));
    }

    public TrasladoResponseDTO actualizar(Long id, TrasladoRequestDTO dto) {
        Traslado traslado = repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Traslado no encontrado con id: " + id));

        validarReglas(dto);
        validarProductoExiste(dto.getProductoId());
        validarUbicacionExiste(dto.getUbicOrigen(), "origen");
        validarUbicacionExiste(dto.getUbicDestino(), "destino");

        traslado.setProductoId(dto.getProductoId());
        traslado.setUbicOrigen(dto.getUbicOrigen());
        traslado.setUbicDestino(dto.getUbicDestino());
        traslado.setCantidad(dto.getCantidad());
        traslado.setMotivo(dto.getMotivo());

        log.info("Actualizando traslado con id {}", id);
        return toResponseDTO(repository.save(traslado));
    }

    private void validarReglas(TrasladoRequestDTO dto) {
        if (dto.getUbicOrigen().equals(dto.getUbicDestino())) {
            throw new IllegalArgumentException("La ubicación origen y destino no pueden ser iguales");
        }
    }

    private void validarProductoExiste(Long productoId) {
        try {
            webClientBuilder.build()
                    .get()
                    .uri(productosUrl + "/api/productos/" + productoId)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            throw new RecursoNoEncontradoException("Producto no encontrado con id: " + productoId);
        }
    }

    private void validarUbicacionExiste(Long ubicacionId, String tipo) {
        try {
            webClientBuilder.build()
                    .get()
                    .uri(ubicacionesUrl + "/api/ubicaciones/" + ubicacionId)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            throw new RecursoNoEncontradoException("Ubicación " + tipo + " no encontrada con id: " + ubicacionId);
        }
    }

    private TrasladoResponseDTO toResponseDTO(Traslado traslado) {
        return new TrasladoResponseDTO(
                traslado.getId(),
                traslado.getProductoId(),
                traslado.getUbicOrigen(),
                traslado.getUbicDestino(),
                traslado.getCantidad(),
                traslado.getFecha(),
                traslado.getMotivo()
        );
    }
}