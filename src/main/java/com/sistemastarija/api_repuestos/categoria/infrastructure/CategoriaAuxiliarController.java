package com.sistemastarija.api_repuestos.categoria.infrastructure;

import com.sistemastarija.api_repuestos.categoria.application.dto.CategoriaRequestDTO;
import com.sistemastarija.api_repuestos.categoria.application.dto.CategoriaResponseDTO;
import com.sistemastarija.api_repuestos.categoria.application.service.CategoriaAuxiliarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
public class CategoriaAuxiliarController {

    private final CategoriaAuxiliarService categoriaAuxiliarService;

    @GetMapping("/findAll")
    public ResponseEntity<Map<String, Object>> findAll() {
        List<CategoriaResponseDTO> categorias = categoriaAuxiliarService.findAll();
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", true);
        response.put("message", "Lista de categorías obtenida exitosamente");
        response.put("data", categorias);
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> create(@Valid @RequestBody CategoriaRequestDTO request) {
        CategoriaResponseDTO categoria = categoriaAuxiliarService.create(request);
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", true);
        response.put("message", "Categoría creada/encontrada exitosamente");
        response.put("data", categoria);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
