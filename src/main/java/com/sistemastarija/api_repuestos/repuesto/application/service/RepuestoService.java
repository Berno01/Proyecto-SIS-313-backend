package com.sistemastarija.api_repuestos.repuesto.application.service;
import com.sistemastarija.api_repuestos.repuesto.application.port.in.*;
import com.sistemastarija.api_repuestos.repuesto.application.port.out.RepuestoPersistancePort;
import com.sistemastarija.api_repuestos.repuesto.domain.exception.RepuestoException;
import com.sistemastarija.api_repuestos.repuesto.domain.model.Repuesto;
import com.sistemastarija.api_repuestos.repuesto.infrastructure.adapter.in.dto.RepuestoBusquedaDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RepuestoService implements CreateRepuestoUseCase, UpdateRepuestoUseCase, FindRepuestoUseCase, DeleteRepuestoUseCase, ObtenerCatalogoBusquedaUseCase {
    private final RepuestoPersistancePort repuestoPersistancePort;


    @Transactional
    @Override
    public Repuesto save(Repuesto repuesto) {
        // Validar categorías
        if (!repuestoPersistancePort.validateCategorias(repuesto.getIdsCategorias())) {
            throw new RepuestoException("Una o más categorías no existen");
        }

        // Validar sistema si se proporciona
        if (repuesto.getIdSistema() != null && !repuestoPersistancePort.validateSistema(repuesto.getIdSistema())) {
            throw new RepuestoException("El sistema especificado no existe");
        }

        // Validar vehículos si hay compatibilidades
        if (repuesto.getCompatibilidades() != null && !repuesto.getCompatibilidades().isEmpty()) {
            List<Integer> idsVehiculos = repuesto.getCompatibilidades().stream()
                    .map(c -> c.getVehiculoId())
                    .collect(Collectors.toList());
            
            if (!repuestoPersistancePort.validateVehiculos(idsVehiculos)) {
                throw new RepuestoException("Uno o más vehículos no existen");
            }
        }

        return repuestoPersistancePort.save(repuesto);
    }

    @Override
    public boolean delete(Integer idRepuesto) {
        Optional<Repuesto> repuestoOp = repuestoPersistancePort.findById(idRepuesto);
        Repuesto repuestoDb = repuestoOp.orElseThrow(() ->
                new RepuestoException("Repuesto no encontrado."));
        repuestoDb.setEstadoRepuesto(false);
        repuestoPersistancePort.save(repuestoDb);
        return true;
    }

    @Override
    public Optional<Repuesto> findById(Integer idRepuesto) {
        return repuestoPersistancePort.findById(idRepuesto);
    }

    @Override
    public List<Repuesto> findAll() {
        return repuestoPersistancePort.findAll();
    }

    @Transactional
    @Override
    public Repuesto update(Repuesto repuesto) {
        Optional<Repuesto> repuestoOp = repuestoPersistancePort.findById(repuesto.getIdRepuesto());
        Repuesto repuestoDb = repuestoOp.orElseThrow(() ->
                new RepuestoException("Repuesto no encontrado."));

        // Validar categorías
        if (!repuestoPersistancePort.validateCategorias(repuesto.getIdsCategorias())) {
            throw new RepuestoException("Una o más categorías no existen");
        }

        // Validar sistema si se proporciona
        if (repuesto.getIdSistema() != null && !repuestoPersistancePort.validateSistema(repuesto.getIdSistema())) {
            throw new RepuestoException("El sistema especificado no existe");
        }

        // Validar vehículos si hay compatibilidades
        if (repuesto.getCompatibilidades() != null && !repuesto.getCompatibilidades().isEmpty()) {
            List<Integer> idsVehiculos = repuesto.getCompatibilidades().stream()
                    .map(c -> c.getVehiculoId())
                    .collect(Collectors.toList());
            
            if (!repuestoPersistancePort.validateVehiculos(idsVehiculos)) {
                throw new RepuestoException("Uno o más vehículos no existen");
            }
        }

        repuestoDb.setNombreRepuesto(repuesto.getNombreRepuesto());
        repuestoDb.setStockActual(repuesto.getStockActual());
        repuestoDb.setCostoRepuesto(repuesto.getCostoRepuesto());
        repuestoDb.setPrecioSugerido(repuesto.getPrecioSugerido());
        repuestoDb.setTagsBusqueda(repuesto.getTagsBusqueda());
        repuestoDb.setIdSistema(repuesto.getIdSistema());
        repuestoDb.setIdsCategorias(repuesto.getIdsCategorias());
        repuestoDb.setCompatibilidades(repuesto.getCompatibilidades());
        
        return repuestoPersistancePort.save(repuestoDb);
    }

    @Transactional(readOnly = true)
    @Override
    public List<RepuestoBusquedaDTO> obtenerCatalogo() {
        return repuestoPersistancePort.findAllForCatalogoBusqueda();
    }
}
