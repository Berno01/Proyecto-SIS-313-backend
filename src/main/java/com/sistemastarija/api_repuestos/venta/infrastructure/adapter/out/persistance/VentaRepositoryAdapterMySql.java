package com.sistemastarija.api_repuestos.venta.infrastructure.adapter.out.persistance;


import com.sistemastarija.api_repuestos.auth.infrastructure.adapter.out.persistence.repository.UsuarioRepository;
import com.sistemastarija.api_repuestos.venta.application.port.out.VentaPersistantPort;
import com.sistemastarija.api_repuestos.venta.domain.model.Venta;
import com.sistemastarija.api_repuestos.venta.infrastructure.adapter.out.persistance.entity.DetalleVentaEntity;
import com.sistemastarija.api_repuestos.venta.infrastructure.adapter.out.persistance.entity.VentaEntity;
import com.sistemastarija.api_repuestos.venta.infrastructure.adapter.out.persistance.mapper.VentaPersistanceMapper;
import com.sistemastarija.api_repuestos.venta.infrastructure.adapter.out.persistance.repository.VentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class VentaRepositoryAdapterMySql implements VentaPersistantPort {

    private final VentaRepository repository;
    private final VentaPersistanceMapper mapper;
    private final UsuarioRepository usuarioRepository;

    @Override
    public Venta save(Venta ventaDominio) {

        VentaEntity ventaEntity = mapper.toVentaEntity(ventaDominio);

        // Lógica de auditoría
        if (ventaEntity.getId_venta() == null) {
            // Es una CREACIÓN nueva
            java.time.LocalDateTime now = java.time.LocalDateTime.now();
            ventaEntity.setCreatedAt(now);
            ventaEntity.setCreatedBy(ventaDominio.getIdUsuario());
            // También llenar updated_at y updated_by en la creación
            ventaEntity.setUpdatedAt(now);
            ventaEntity.setUpdatedBy(ventaDominio.getIdUsuario());
        } else {
            // Es una ACTUALIZACIÓN - preservar campos de creación
            VentaEntity ventaExistente = repository.findById(Long.valueOf(ventaEntity.getId_venta()))
                    .orElse(null);
            if (ventaExistente != null) {
                // CRÍTICO: NO tocar createdAt ni createdBy
                ventaEntity.setCreatedAt(ventaExistente.getCreatedAt());
                ventaEntity.setCreatedBy(ventaExistente.getCreatedBy());
            }
            // Setear campos de actualización
            ventaEntity.setUpdatedAt(java.time.LocalDateTime.now());
            ventaEntity.setUpdatedBy(ventaDominio.getIdUsuario());
        }

        if (ventaEntity.getDetalle_venta() != null) {
            for (DetalleVentaEntity detalle : ventaEntity.getDetalle_venta()) {
                detalle.setVenta(ventaEntity);
            }
        }

        VentaEntity entityGuardada = repository.save(ventaEntity);

        return mapper.toVentaModel(entityGuardada);
    }

    @Override
    public List<Venta> findAll() {
        List<VentaEntity> ventaEntities = repository.findAllByEstadoVentaTrue();
        
        return ventaEntities.stream()
                .map(entity -> {
                    Venta venta = mapper.toVentaModel(entity);
                    // Obtener el username del usuario que actualizó (o creó si no hay actualización)
                    Integer userId = entity.getUpdatedBy() != null ? entity.getUpdatedBy() : entity.getCreatedBy();
                    if (userId != null) {
                        usuarioRepository.findById(userId)
                                .ifPresent(usuario -> venta.setUsername(usuario.getUsername()));
                    }
                    return venta;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Venta> findById(Integer idVenta) {
        return repository.findById(Long.valueOf(idVenta))
                .map(mapper::toVentaModel);
    }



}
