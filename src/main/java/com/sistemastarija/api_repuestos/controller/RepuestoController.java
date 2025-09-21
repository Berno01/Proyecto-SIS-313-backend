package com.sistemastarija.api_repuestos.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1") // 👈 Prefijo para versionar tu API
public class RepuestoController {

    // Lista hardcodeada de repuestos (simulando base de datos)
    private final List<Map<String, Object>> repuestos = List.of(
            Map.of(
                    "id", 1,
                    "nombre", "Filtro de aceite",
                    "precio", 50.0,
                    "stock", 25
            ),
            Map.of(
                    "id", 2,
                    "nombre", "Filtro de gasolina",
                    "precio", 55.0,
                    "stock", 15
            ),
            Map.of(
                    "id", 3,
                    "nombre", "Pastillas de freno",
                    "precio", 120.0,
                    "stock", 8
            )
    );

    // Lista hardcodeada de inventario (simulando tabla de inventario)
    private final List<Map<String, Object>> inventario = List.of(
            Map.of("repuestoId", 1, "stockDisponible", 25),
            Map.of("repuestoId", 2, "stockDisponible", 15),
            Map.of("repuestoId", 3, "stockDisponible", 8)
    );

    /**
     *  FUNCIÓN 1: Obtener UN repuesto por ID
     * Cambié de getRepuestos (varios) a getRepuesto (uno solo)
     */
    @GetMapping("/repuesto/{id}")
    public ResponseEntity<Map<String, Object>> getRepuesto(@PathVariable Long id) {
        // Buscar el repuesto por ID
        Optional<Map<String, Object>> repuestoEncontrado = repuestos.stream()
                .filter(repuesto -> repuesto.get("id").equals(id.intValue()))
                .findFirst();

        if (repuestoEncontrado.isPresent()) {
            return ResponseEntity.ok(repuestoEncontrado.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     *  FUNCIÓN 2: Validar datos de venta
     * Función privada que valida si hay suficiente inventario
     */
    private boolean validarDatos(Long repuestoId, int cantidadSolicitada) {
        // Buscar en inventario hardcodeado
        Optional<Map<String, Object>> inventarioItem = inventario.stream()
                .filter(item -> item.get("repuestoId").equals(repuestoId.intValue()))
                .findFirst();

        if (inventarioItem.isPresent()) {
            int stockDisponible = (Integer) inventarioItem.get().get("stockDisponible");
            return stockDisponible >= cantidadSolicitada;
        }

        return false; // Si no existe el repuesto, no se puede vender
    }

    /**
     * FUNCIÓN 3: Registrar venta
     * Recibe parámetros, valida y responde OK o ERROR
     */
    @PostMapping("/venta/registrar")
    public ResponseEntity<Map<String, Object>> registrarVenta(@RequestBody Map<String, Object> datosVenta) {
        try {
            // Extraer parámetros básicos (puedes modificar según tus necesidades)
            Long repuestoId = Long.valueOf(datosVenta.get("repuestoId").toString());
            int cantidad = Integer.parseInt(datosVenta.get("cantidad").toString());
            String cliente = datosVenta.get("cliente").toString();

            // Llamar a la función de validación
            boolean validacionExitosa = validarDatos(repuestoId, cantidad);

            if (validacionExitosa) {
                // Validación positiva - Venta exitosa
                return ResponseEntity.ok(Map.of(
                        "status", "SUCCESS",
                        "mensaje", "Venta registrada exitosamente",
                        "codigo", 200,
                        "datos", Map.of(
                                "repuestoId", repuestoId,
                                "cantidad", cantidad,
                                "cliente", cliente,
                                "timestamp", System.currentTimeMillis()
                        )
                ));
            } else {
                // Validación negativa - Sin inventario suficiente
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                        "status", "ERROR",
                        "mensaje", "Inventario insuficiente para completar la venta",
                        "codigo", 400,
                        "repuestoId", repuestoId,
                        "cantidadSolicitada", cantidad
                ));
            }

        } catch (Exception e) {
            // Error en parámetros o procesamiento
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "ERROR",
                    "mensaje", "Error en los datos enviados: " + e.getMessage(),
                    "codigo", 400
            ));
        }
    }

    /**
     *  FUNCIÓN Ver todo el inventario (para debugging)
     */
    @GetMapping("/inventario")
    public ResponseEntity<List<Map<String, Object>>> getInventario() {
        return ResponseEntity.ok(inventario);
    }
}