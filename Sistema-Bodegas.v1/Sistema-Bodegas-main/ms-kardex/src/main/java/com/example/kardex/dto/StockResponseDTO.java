package com.example.kardex.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Respuesta con el stock actual de un producto en una bodega")
public class StockResponseDTO {

    @Schema(description = "ID del producto", example = "1001")
    private Long productoId;

    @Schema(description = "ID de la bodega", example = "2")
    private Long bodegaId;

    @Schema(description = "Cantidad actual en stock", example = "35")
    private int cantidad;
}