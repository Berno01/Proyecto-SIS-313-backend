package com.sistemastarija.api_repuestos.sistemaauto.application.service;

import com.sistemastarija.api_repuestos.repuesto.infrastructure.adapter.out.persistance.entity.SistemaAutoEntity;
import com.sistemastarija.api_repuestos.repuesto.infrastructure.adapter.out.persistance.repository.SistemaAutoRepository;
import com.sistemastarija.api_repuestos.sistemaauto.application.dto.SistemaAutoRequestDTO;
import com.sistemastarija.api_repuestos.sistemaauto.application.dto.SistemaAutoResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SistemaAutoService {
    private final SistemaAutoRepository sistemaAutoRepository;

    @Transactional(readOnly = true)
    public List<SistemaAutoResponseDTO> findAll() {
        return sistemaAutoRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public SistemaAutoResponseDTO create(SistemaAutoRequestDTO request) {
        // Normalización: Trim y Mayúsculas
        String nombreNormalizado = request.getNombreSistema().trim().toUpperCase();

        // Verificar si ya existe
        return sistemaAutoRepository.findByNombreSistema(nombreNormalizado)
                .map(this::toResponseDTO) // Si existe, devolver el existente
                .orElseGet(() -> {
                    // Si no existe, crear nuevo
                    SistemaAutoEntity newSistema = new SistemaAutoEntity();
                    newSistema.setNombreSistema(nombreNormalizado);
                    SistemaAutoEntity saved = sistemaAutoRepository.save(newSistema);
                    return toResponseDTO(saved);
                });
    }

    private SistemaAutoResponseDTO toResponseDTO(SistemaAutoEntity entity) {
        SistemaAutoResponseDTO dto = new SistemaAutoResponseDTO();
        dto.setId(entity.getIdSistema());
        dto.setNombre(entity.getNombreSistema());
        return dto;
    }
}
