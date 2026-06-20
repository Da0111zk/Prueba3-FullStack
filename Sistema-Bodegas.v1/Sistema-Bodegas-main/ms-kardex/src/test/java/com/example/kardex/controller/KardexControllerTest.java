package com.example.kardex.controller;

import com.example.kardex.dto.MovimientoRequestDTO;
import com.example.kardex.dto.MovimientoResponseDTO;
import com.example.kardex.dto.StockResponseDTO;
import com.example.kardex.service.KardexService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(KardexController.class)
class KardexControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private KardexService service;

    @Test
    @DisplayName("Debe listar movimientos correctamente")
    void listarMovimientos_ok() throws Exception {
        MovimientoResponseDTO movimiento = new MovimientoResponseDTO();
        movimiento.setId(1L);
        movimiento.setProductoId(100L);
        movimiento.setBodegaId(1L);
        movimiento.setTipoMovimiento("INGRESO");
        movimiento.setCantidad(10);

        when(service.listarMovimientos()).thenReturn(List.of(movimiento));

        mockMvc.perform(get("/api/kardex/movimientos"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe obtener un movimiento por id")
    void obtenerMovimientoPorId_ok() throws Exception {
        MovimientoResponseDTO movimiento = new MovimientoResponseDTO();
        movimiento.setId(1L);
        movimiento.setProductoId(100L);
        movimiento.setBodegaId(1L);
        movimiento.setTipoMovimiento("INGRESO");
        movimiento.setCantidad(10);

        when(service.obtenerMovimientoPorId(1L)).thenReturn(movimiento);

        mockMvc.perform(get("/api/kardex/movimientos/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe registrar un movimiento")
    void registrarMovimiento_created() throws Exception {
        MovimientoRequestDTO request = new MovimientoRequestDTO();
        request.setProductoId(100L);
        request.setBodegaId(1L);
        request.setTipoMovimiento("INGRESO");
        request.setCantidad(10);
        request.setReferencia("Compra inicial");

        MovimientoResponseDTO response = new MovimientoResponseDTO();
        response.setId(1L);
        response.setProductoId(100L);
        response.setBodegaId(1L);
        response.setTipoMovimiento("INGRESO");
        response.setCantidad(10);
        response.setStockAnterior(20);
        response.setStockNuevo(30);
        response.setReferencia("Compra inicial");

        when(service.registrarMovimiento(any(MovimientoRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/kardex/movimientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Debe actualizar un movimiento")
    void actualizarMovimiento_ok() throws Exception {
        MovimientoRequestDTO request = new MovimientoRequestDTO();
        request.setProductoId(100L);
        request.setBodegaId(1L);
        request.setTipoMovimiento("AJUSTE");
        request.setCantidad(5);
        request.setSigno("POSITIVO");
        request.setReferencia("Ajuste manual");

        MovimientoResponseDTO response = new MovimientoResponseDTO();
        response.setId(1L);
        response.setProductoId(100L);
        response.setBodegaId(1L);
        response.setTipoMovimiento("AJUSTE");
        response.setCantidad(5);
        response.setSigno("POSITIVO");
        response.setReferencia("Ajuste manual");

        when(service.actualizarMovimiento(eq(1L), any(MovimientoRequestDTO.class))).thenReturn(response);

        mockMvc.perform(put("/api/kardex/movimientos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe eliminar un movimiento")
    void eliminarMovimiento_noContent() throws Exception {
        doNothing().when(service).eliminarMovimiento(1L);

        mockMvc.perform(delete("/api/kardex/movimientos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Debe obtener stock por producto y bodega")
    void obtenerStock_ok() throws Exception {
        StockResponseDTO stock = new StockResponseDTO();
        stock.setProductoId(100L);
        stock.setBodegaId(1L);
        stock.setCantidad(20);

        when(service.obtenerStock(100L, 1L)).thenReturn(stock);

        mockMvc.perform(get("/api/kardex/stock/producto/100/bodega/1"))
                .andExpect(status().isOk());
    }
}