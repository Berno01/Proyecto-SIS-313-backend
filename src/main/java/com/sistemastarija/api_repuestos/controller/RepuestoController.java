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
                    "nombre_repuesto", "Filtro de aceite",
                    "precio_compra", 35.0,
                    "precio_venta", 50.0,
                    "stock_repuesto", 25
            ),
            Map.of(
                    "id", 2,
                    "nombre_repuesto", "Filtro de gasolina",
                    "precio_compra", 35.0,
                    "precio_venta", 50.0,
                    "stock_repuesto", 15
            ),
            Map.of(
                    "id", 3,
                    "nombre_repuesto", "Pastillas de freno",
                    "precio_compra", 35.0,
                    "precio_venta", 50.0,
                    "stock_repuesto", 8
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
            // Leer los datos generales
            String cliente = datosVenta.get("nombre_cliente").toString();
            Double totalVenta = Double.valueOf(datosVenta.get("total_venta").toString());
            Object idVenta = datosVenta.get("id_venta"); // normalmente null

            // Leer detalle_venta como lista
            List<Map<String, Object>> detalles = (List<Map<String, Object>>) datosVenta.get("detalle_venta");

            // Ejemplo de validación recorriendo los detalles
            for (Map<String, Object> det : detalles) {
                Long repuestoId = Long.valueOf(det.get("id_repuesto").toString());
                int cantidad = Integer.parseInt(det.get("cantidad").toString());
                Double precioCompra = Double.valueOf(det.get("precio_compra").toString());
                Double precioVenta = Double.valueOf(det.get("precio_venta").toString());
                Double total = Double.valueOf(det.get("total").toString());

                // 👉 aquí podrías validar inventario por cada repuesto
                System.out.println("Detalle repuesto: " + repuestoId + " cantidad=" + cantidad);
            }

            // Si todo OK, devolver respuesta
            return ResponseEntity.ok(Map.of(
                    "status", "SUCCESS",
                    "mensaje", "Venta registrada exitosamente",
                    "codigo", 200,
                    "datos", datosVenta
            ));

        } catch (Exception e) {
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