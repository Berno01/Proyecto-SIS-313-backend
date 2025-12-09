package com.sistemastarija.api_repuestos.vehiculo.application.service;

import com.sistemastarija.api_repuestos.repuesto.infrastructure.adapter.out.persistance.entity.VehiculoEntity;
import com.sistemastarija.api_repuestos.repuesto.infrastructure.adapter.out.persistance.repository.VehiculoRepository;
import com.sistemastarija.api_repuestos.vehiculo.application.dto.VehiculoRequestDTO;
import com.sistemastarija.api_repuestos.vehiculo.application.dto.VehiculoResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VehiculoService {
    private final VehiculoRepository vehiculoRepository;

    @Transactional(readOnly = true)
    public List<VehiculoResponseDTO> findAll() {
        return vehiculoRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public VehiculoResponseDTO create(VehiculoRequestDTO request) {
        // Normalización: Trim y Mayúsculas
        String marcaNormalizada = request.getMarca().trim().toUpperCase();
        String modeloNormalizado = request.getModelo().trim().toUpperCase();

        // Verificar si ya existe
        return vehiculoRepository.findByMarcaAndModelo(marcaNormalizada, modeloNormalizado)
                .map(this::toResponseDTO) // Si existe, devolver el existente
                .orElseGet(() -> {
                    // Si no existe, crear nuevo
                    VehiculoEntity newVehiculo = new VehiculoEntity();
                    newVehiculo.setMarca(marcaNormalizada);
                    newVehiculo.setModelo(modeloNormalizado);
                    VehiculoEntity saved = vehiculoRepository.save(newVehiculo);
                    return toResponseDTO(saved);
                });
    }

    private VehiculoResponseDTO toResponseDTO(VehiculoEntity entity) {
        VehiculoResponseDTO dto = new VehiculoResponseDTO();
        dto.setId(entity.getIdVehiculo());
        dto.setMarca(entity.getMarca());
        dto.setModelo(entity.getModelo());
        return dto;
    }
}
