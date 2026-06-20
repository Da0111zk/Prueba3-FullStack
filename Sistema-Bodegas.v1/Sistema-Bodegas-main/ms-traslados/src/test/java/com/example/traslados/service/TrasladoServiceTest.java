package com.example.traslados.service;

import com.example.traslados.dto.TrasladoRequestDTO;
import com.example.traslados.dto.TrasladoResponseDTO;
import com.example.traslados.exception.RecursoNoEncontradoException;
import com.example.traslados.model.Traslado;
import com.example.traslados.repository.TrasladoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
@ExtendWith(MockitoExtension.class)
class TrasladoServiceTest {

    @Mock TrasladoRepository repository;
    @Mock WebClient.Builder webClientBuilder;
    @Mock WebClient webClient;
    @InjectMocks TrasladoService service;

    @Mock WebClient.RequestHeadersUriSpec getUriSpec;
    @Mock WebClient.RequestHeadersSpec headersSpec;
    @Mock WebClient.ResponseSpec responseSpec;

    private void setUpWebClient() {
        ReflectionTestUtils.setField(service, "productosUrl", "http://localhost:8081");
        ReflectionTestUtils.setField(service, "ubicacionesUrl", "http://localhost:8082");
        when(webClientBuilder.build()).thenReturn(webClient);
    }

    private void mockValidacionesExternasExitosas() {
        when(webClient.get()).thenReturn(getUriSpec);
        when(getUriSpec.uri(anyString())).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just("OK"));
    }

    private TrasladoRequestDTO dtoBase() {
        TrasladoRequestDTO dto = new TrasladoRequestDTO();
        dto.setProductoId(1L);
        dto.setUbicOrigen(10L);
        dto.setUbicDestino(20L);
        dto.setCantidad(5);
        dto.setMotivo("Reabastecimiento");
        return dto;
    }

    private Traslado trasladoMock(Long id) {
        Traslado t = new Traslado();
        t.setId(id);
        t.setProductoId(1L);
        t.setUbicOrigen(10L);
        t.setUbicDestino(20L);
        t.setCantidad(5);
        t.setFecha(LocalDateTime.now());
        t.setMotivo("Reabastecimiento");
        return t;
    }

    @Test
    void crear_exito() {
        setUpWebClient();
        mockValidacionesExternasExitosas();
        when(repository.save(any())).thenReturn(trasladoMock(100L));

        TrasladoResponseDTO resp = service.crear(dtoBase());

        assertThat(resp.getId()).isEqualTo(100L);
        assertThat(resp.getProductoId()).isEqualTo(1L);
        verify(repository).save(any());
    }

    @Test
    void crear_origenIgualDestino_lanzaExcepcion() {
        TrasladoRequestDTO dto = dtoBase();
        dto.setUbicDestino(10L);

        assertThatThrownBy(() -> service.crear(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("no pueden ser iguales");
    }

    @Test
    void crear_productoNoExiste_lanzaExcepcion() {
        setUpWebClient();
        when(webClient.get()).thenReturn(getUriSpec);
        when(getUriSpec.uri(contains("productos"))).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.error(new RuntimeException()));

        assertThatThrownBy(() -> service.crear(dtoBase()))
                .isInstanceOf(RecursoNoEncontradoException.class)
                .hasMessageContaining("Producto no encontrado");
    }

    @Test
    void crear_ubicacionOrigenNoExiste_lanzaExcepcion() {
        setUpWebClient();
        when(webClient.get()).thenReturn(getUriSpec);
        when(getUriSpec.uri(anyString())).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class))
                .thenReturn(Mono.just("OK"))
                .thenReturn(Mono.error(new RuntimeException()));

        assertThatThrownBy(() -> service.crear(dtoBase()))
                .isInstanceOf(RecursoNoEncontradoException.class)
                .hasMessageContaining("Ubicación origen no encontrada");
    }

    @Test
    void crear_ubicacionDestinoNoExiste_lanzaExcepcion() {
        setUpWebClient();
        when(webClient.get()).thenReturn(getUriSpec);
        when(getUriSpec.uri(anyString())).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class))
                .thenReturn(Mono.just("OK"))
                .thenReturn(Mono.just("OK"))
                .thenReturn(Mono.error(new RuntimeException()));

        assertThatThrownBy(() -> service.crear(dtoBase()))
                .isInstanceOf(RecursoNoEncontradoException.class)
                .hasMessageContaining("Ubicación destino no encontrada");
    }

    @Test
    void actualizar_exito() {
        setUpWebClient();
        when(repository.findById(1L)).thenReturn(Optional.of(trasladoMock(1L)));
        mockValidacionesExternasExitosas();
        when(repository.save(any())).thenReturn(trasladoMock(1L));

        TrasladoResponseDTO resp = service.actualizar(1L, dtoBase());

        assertThat(resp.getId()).isEqualTo(1L);
        verify(repository).save(any());
    }

    @Test
    void actualizar_noEncontrado_lanzaExcepcion() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.actualizar(99L, dtoBase()))
                .isInstanceOf(RecursoNoEncontradoException.class)
                .hasMessageContaining("Traslado no encontrado");
    }

    @Test
    void listarTodos_retornaLista() {
        when(repository.findAll()).thenReturn(List.of(trasladoMock(1L), trasladoMock(2L)));

        assertThat(service.listarTodos()).hasSize(2);
    }

    @Test
    void obtenerPorId_encontrado() {
        when(repository.findById(1L)).thenReturn(Optional.of(trasladoMock(1L)));

        TrasladoResponseDTO resp = service.obtenerPorId(1L);

        assertThat(resp.getId()).isEqualTo(1L);
    }

    @Test
    void obtenerPorId_noEncontrado_lanzaExcepcion() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.obtenerPorId(99L))
                .isInstanceOf(RecursoNoEncontradoException.class)
                .hasMessageContaining("Traslado no encontrado");
    }

    @Test
    void obtenerPorProducto_retornaLista() {
        when(repository.findByProductoId(1L)).thenReturn(List.of(trasladoMock(1L)));

        assertThat(service.obtenerPorProducto(1L)).hasSize(1);
    }
}