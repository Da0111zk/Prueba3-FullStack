package com.example.ms_producto.service;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.ms_producto.modelo.Producto;
import com.example.ms_producto.repository.ProductoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;

    @Qualifier("webClientCategorias")
    private final WebClient webClientCategorias;

    public List<Producto> obtenerTodos() {
        return productoRepository.findAll();
    }

    public Optional<Producto> obtenerPorId(Long productoId) {
        return productoRepository.findById(productoId);
    }

    public Producto guardar(Producto producto) {
        validarCategoriaExiste(producto.getCategoriaId());
        return productoRepository.save(producto);
    }

    public void eliminar(Long productoId) {
        productoRepository.deleteById(productoId);
    }

    private void validarCategoriaExiste(Long categoriaId) {
        if (categoriaId == null) return;
        log.info("Validando categoriaId={} en ms-categorias", categoriaId);
        try {
            webClientCategorias.get()
                    .uri("/api/categorias/" + categoriaId)
                    .retrieve()
                    .bodyToMono(Object.class)
                    .timeout(Duration.ofSeconds(3))
                    .block();
        } catch (Exception e) {
            log.error("Categoria {} no encontrada: {}", categoriaId, e.getMessage());
            throw new RuntimeException(
                    "Categoria con id " + categoriaId + " no existe en ms-categorias");
        }
    }
}