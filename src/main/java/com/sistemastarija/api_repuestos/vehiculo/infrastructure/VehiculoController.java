package com.sistemastarija.api_repuestos.vehiculo.infrastructure;

import com.sistemastarija.api_repuestos.vehiculo.application.dto.VehiculoRequestDTO;
import com.sistemastarija.api_repuestos.vehiculo.application.dto.VehiculoResponseDTO;
import com.sistemastarija.api_repuestos.vehiculo.application.service.VehiculoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vehiculos")
@RequiredArgsConstructor
public class VehiculoController {

    private final VehiculoService vehiculoService;

    @GetMapping("/findAll")
    public ResponseEntity<Map<String, Object>> findAll() {
        List<VehiculoResponseDTO> vehiculos = vehiculoService.findAll();
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", true);
        response.put("message", "Lista de vehículos obtenida exitosamente");
        response.put("data", vehiculos);
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> create(@Valid @RequestBody VehiculoRequestDTO request) {
        VehiculoResponseDTO vehiculo = vehiculoService.create(request);
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", true);
        response.put("message", "Vehículo creado/encontrado exitosamente");
        response.put("data", vehiculo);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
