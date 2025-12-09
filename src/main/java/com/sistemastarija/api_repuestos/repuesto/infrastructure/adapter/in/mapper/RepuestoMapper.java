package com.sistemastarija.api_repuestos.repuesto.infrastructure.adapter.in.mapper;

import com.sistemastarija.api_repuestos.repuesto.domain.model.Compatibilidad;
import com.sistemastarija.api_repuestos.repuesto.domain.model.Repuesto;
import com.sistemastarija.api_repuestos.repuesto.infrastructure.adapter.in.dto.CompatibilidadRequestDTO;
import com.sistemastarija.api_repuestos.repuesto.infrastructure.adapter.in.dto.RepuestoRequestDTO;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RepuestoMapper {
    public Repuesto toDomain(RepuestoRequestDTO dto){
        List<Compatibilidad> compatibilidades = null;
        if (dto.getCompatibilidades() != null && !dto.getCompatibilidades().isEmpty()) {
            compatibilidades = dto.getCompatibilidades().stream()
                    .map(this::toCompatibilidadDomain)
                    .collect(Collectors.toList());
        }

        return new Repuesto(
                dto.getIdRepuesto(),
                dto.getNombreRepuesto(),
                dto.getStockActual(),
                dto.getCostoRepuesto(),
                dto.getPrecioSugerido(),
                dto.getEstadoRepuesto(),
                dto.getTagsBusqueda(),
                dto.getIdSistema(),
                dto.getIdsCategorias(),
                compatibilidades
        );
    }

    private Compatibilidad toCompatibilidadDomain(CompatibilidadRequestDTO dto) {
        return new Compatibilidad(
                null, // El ID se generará en la BD
                dto.getVehiculoId(),
                dto.getAnioInicio(),
                dto.getAnioFin(),
                dto.getNotas()
        );
    }
}
