package com.sistemastarija.api_repuestos.categoria.application.service;

import com.sistemastarija.api_repuestos.categoria.application.dto.CategoriaRequestDTO;
import com.sistemastarija.api_repuestos.categoria.application.dto.CategoriaResponseDTO;
import com.sistemastarija.api_repuestos.categoria.infrastructure.adapter.out.persistance.entity.CategoriaEntity;
import com.sistemastarija.api_repuestos.categoria.infrastructure.adapter.out.persistance.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoriaAuxiliarService {
    private final CategoriaRepository categoriaRepository;

    @Transactional(readOnly = true)
    public List<CategoriaResponseDTO> findAll() {
        return categoriaRepository.findAllByEstadoCategoriaTrue().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public CategoriaResponseDTO create(CategoriaRequestDTO request) {
        // Normalización: Trim y Mayúsculas
        String nombreNormalizado = request.getNombreCategoria().trim().toUpperCase();

        // Verificar si ya existe
        return categoriaRepository.findByNombreCategoria(nombreNormalizado)
                .map(this::toResponseDTO) // Si existe, devolver el existente
                .orElseGet(() -> {
                    // Si no existe, crear nuevo
                    CategoriaEntity newCategoria = new CategoriaEntity();
                    newCategoria.setNombre_categoria(nombreNormalizado);
                    newCategoria.setEstadoCategoria(true); // Activo por defecto
                    CategoriaEntity saved = categoriaRepository.save(newCategoria);
                    return toResponseDTO(saved);
                });
    }

    private CategoriaResponseDTO toResponseDTO(CategoriaEntity entity) {
        CategoriaResponseDTO dto = new CategoriaResponseDTO();
        dto.setId(entity.getIdCategoria());
        dto.setNombre(entity.getNombre_categoria());
        return dto;
    }
}
