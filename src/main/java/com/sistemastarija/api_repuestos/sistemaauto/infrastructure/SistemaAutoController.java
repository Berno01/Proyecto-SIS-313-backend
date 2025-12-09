package com.sistemastarija.api_repuestos.sistemaauto.infrastructure;

import com.sistemastarija.api_repuestos.sistemaauto.application.dto.SistemaAutoRequestDTO;
import com.sistemastarija.api_repuestos.sistemaauto.application.dto.SistemaAutoResponseDTO;
import com.sistemastarija.api_repuestos.sistemaauto.application.service.SistemaAutoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sistemas")
@RequiredArgsConstructor
public class SistemaAutoController {

    private final SistemaAutoService sistemaAutoService;

    @GetMapping("/findAll")
    public ResponseEntity<Map<String, Object>> findAll() {
        List<SistemaAutoResponseDTO> sistemas = sistemaAutoService.findAll();
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", true);
        response.put("message", "Lista de sistemas obtenida exitosamente");
        response.put("data", sistemas);
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> create(@Valid @RequestBody SistemaAutoRequestDTO request) {
        SistemaAutoResponseDTO sistema = sistemaAutoService.create(request);
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", true);
        response.put("message", "Sistema creado/encontrado exitosamente");
        response.put("data", sistema);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
