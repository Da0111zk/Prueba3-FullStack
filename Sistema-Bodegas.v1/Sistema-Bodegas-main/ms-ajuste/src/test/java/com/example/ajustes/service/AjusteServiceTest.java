package com.example.ajustes.service;

import com.example.ajustes.dto.AjusteRequestDTO;
import com.example.ajustes.dto.AjusteResponseDTO;
import com.example.ajustes.model.Ajuste;
import com.example.ajustes.repository.AjusteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
@ExtendWith(MockitoExtension.class)
class AjusteServiceTest {

    @Mock AjusteRepository repository;
    @Mock WebClient webClientProductos;
    @Mock WebClient webClientKardex;
    @InjectMocks AjusteService service;

    @Mock WebClient.RequestHeadersUriSpec getUriSpec;
    @Mock WebClient.RequestHeadersSpec headersSpec;
    @Mock WebClient.ResponseSpec responseSpec;
    @Mock WebClient.RequestBodyUriSpec postUriSpec;
    @Mock WebClient.RequestBodySpec bodySpec;

    private void mockProductoExiste() {
        when(webClientProductos.get()).thenReturn(getUriSpec);
        when(getUriSpec.uri(anyString())).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Object.class)).thenReturn(Mono.just(new Object()));
    }

    private void mockStock(int cantidad) {
        when(webClientKardex.get()).thenReturn(getUriSpec);
        when(getUriSpec.uri(contains("stock"))).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(any(ParameterizedTypeReference.class)))
                .thenReturn(Mono.just(Map.of("cantidad", cantidad)));
    }

    private void mockPostKardex(boolean success) {
        when(webClientKardex.post()).thenReturn(postUriSpec);
        when(postUriSpec.uri(anyString())).thenReturn(bodySpec);
        when(bodySpec.bodyValue(any())).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        Mono<Object> mono = success ? Mono.just(new Object()) : Mono.error(new RuntimeException());
        when(responseSpec.bodyToMono(Object.class)).thenReturn(mono);
    }

    private AjusteRequestDTO dtoBase(int cantidad) {
        AjusteRequestDTO dto = new AjusteRequestDTO();
        dto.setProductoId(1L);
        dto.setBodegaId(2L);
        dto.setCantidadAjuste(cantidad);
        dto.setMotivo("Inventario");
        dto.setResponsable("Admin");
        dto.setObservaciones("Test");
        return dto;
    }

    private Ajuste ajusteMock(Long id, String estado, int cantidadAjuste, int stockAnterior, int stockNuevo) {
        Ajuste a = new Ajuste();
        a.setId(id);
        a.setProductoId(1L);
        a.setBodegaId(2L);
        a.setCantidadAjuste(cantidadAjuste);
        a.setStockAnterior(stockAnterior);
        a.setStockNuevo(stockNuevo);
        a.setFechaAjuste(LocalDate.now());
        a.setMotivo("Inventario");
        a.setResponsable("Admin");
        a.setEstado(estado);
        a.setObservaciones("Test");
        return a;
    }

    @Test
    void crear_ajustePositivo_exito() {
        mockProductoExiste();
        mockStock(10);
        when(repository.save(any())).thenReturn(ajusteMock(100L, "PENDIENTE", 5, 10, 15));

        AjusteResponseDTO resp = service.crear(dtoBase(5));

        assertThat(resp.getId()).isEqualTo(100L);
        assertThat(resp.getStockAnterior()).isEqualTo(10);
        assertThat(resp.getStockNuevo()).isEqualTo(15);
        assertThat(resp.getEstado()).isEqualTo("PENDIENTE");
    }

    @Test
    void crear_ajusteNegativo_exito() {
        mockProductoExiste();
        mockStock(10);
        when(repository.save(any())).thenReturn(ajusteMock(101L, "PENDIENTE", -3, 10, 7));

        AjusteResponseDTO resp = service.crear(dtoBase(-3));

        assertThat(resp.getStockAnterior()).isEqualTo(10);
        assertThat(resp.getStockNuevo()).isEqualTo(7);
    }

    @Test
    void crear_cantidadCero_lanzaExcepcion() {
        assertThatThrownBy(() -> service.crear(dtoBase(0)))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("no puede ser cero");
    }

    @Test
    void crear_productoNoExiste_lanzaExcepcion() {
        when(webClientProductos.get()).thenReturn(getUriSpec);
        when(getUriSpec.uri(anyString())).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Object.class)).thenReturn(Mono.error(new RuntimeException()));

        assertThatThrownBy(() -> service.crear(dtoBase(5)))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("no existe en ms-productos");
    }

    @Test
    void crear_stockNegativo_lanzaExcepcion() {
        mockProductoExiste();
        mockStock(5);

        assertThatThrownBy(() -> service.crear(dtoBase(-10)))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("stock negativo");
    }

    @Test
    void aprobar_exito() {
        Ajuste pendiente = ajusteMock(10L, "PENDIENTE", 5, 10, 15);
        when(repository.findById(10L)).thenReturn(Optional.of(pendiente));
        when(repository.save(any())).thenReturn(pendiente);
        mockPostKardex(true);

        AjusteResponseDTO resp = service.aprobar(10L);

        assertThat(resp.getEstado()).isEqualTo("APROBADO");
        verify(webClientKardex).post();
    }

    @Test
    void aprobar_falloKardex_lanzaExcepcion() {
        Ajuste pendiente = ajusteMock(11L, "PENDIENTE", 3, 10, 13);
        when(repository.findById(11L)).thenReturn(Optional.of(pendiente));
        mockPostKardex(false);

        assertThatThrownBy(() -> service.aprobar(11L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("No se pudo registrar el movimiento en Kardex");

        verify(repository, never()).save(any());
    }

    @Test
    void aprobar_estadoNoPendiente_lanzaExcepcion() {
        when(repository.findById(12L)).thenReturn(Optional.of(ajusteMock(12L, "APROBADO", 5, 10, 15)));

        assertThatThrownBy(() -> service.aprobar(12L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Solo se pueden aprobar ajustes en estado PENDIENTE");
    }

    @Test
    void rechazar_exito() {
        when(repository.findById(20L)).thenReturn(Optional.of(ajusteMock(20L, "PENDIENTE", 5, 10, 15)));
        when(repository.save(any())).thenReturn(ajusteMock(20L, "RECHAZADO", 5, 10, 15));

        assertThat(service.rechazar(20L).getEstado()).isEqualTo("RECHAZADO");
    }

    @Test
    void rechazar_estadoNoPendiente_lanzaExcepcion() {
        when(repository.findById(21L)).thenReturn(Optional.of(ajusteMock(21L, "APROBADO", 5, 10, 15)));

        assertThatThrownBy(() -> service.rechazar(21L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Solo se pueden rechazar ajustes en estado PENDIENTE");
    }

    @Test
    void listarTodos_retornaLista() {
        when(repository.findAll()).thenReturn(List.of(
                ajusteMock(1L, "PENDIENTE", 5, 10, 15),
                ajusteMock(2L, "APROBADO", -3, 20, 17)
        ));

        assertThat(service.listarTodos()).hasSize(2);
    }

    @Test
    void buscarPorId_noEncontrado_lanzaExcepcion() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.buscarPorId(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Ajuste no encontrado");
    }

    @Test
    void listarPendientes_soloPendientes() {
        when(repository.findByEstado("PENDIENTE")).thenReturn(List.of(ajusteMock(1L, "PENDIENTE", 5, 10, 15)));

        assertThat(service.listarPendientes()).hasSize(1);
    }
}