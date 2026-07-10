package com.bodega.gateway.controller;
 
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
 
import java.time.LocalDateTime;
import java.util.Map;
 
@RestController
public class FallbackController {
 
    // Un solo endpoint cubre las 10 rutas de fallback del YAML
    @GetMapping("/fallback/{servicio}")
    public ResponseEntity<Map<String, Object>> fallback(@PathVariable String servicio) {
        Map<String, Object> body = Map.of(
                "timestamp", LocalDateTime.now(),
                "status", 503,
                "error", "Servicio no disponible",
                "servicio", servicio,
                "mensaje", "El microservicio '" + servicio + "' no está respondiendo en este momento. Intenta nuevamente en unos segundos."
        );
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(body);
    }
}