package com.sistemastarija.api_repuestos.repuesto.infrastructure.adapter.in.mapper;

import com.sistemastarija.api_repuestos.repuesto.infrastructure.adapter.in.dto.RepuestoBusquedaDTO;
import com.sistemastarija.api_repuestos.repuesto.infrastructure.adapter.out.persistance.entity.CompatibilidadEntity;
import com.sistemastarija.api_repuestos.repuesto.infrastructure.adapter.out.persistance.entity.RepuestoEntity;
import org.springframework.stereotype.Component;

import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class RepuestoBusquedaMapper {

    public RepuestoBusquedaDTO toDTO(RepuestoEntity entity) {
        RepuestoBusquedaDTO dto = new RepuestoBusquedaDTO();
        
        dto.setId(entity.getId_repuesto());
        dto.setNombre(entity.getNombre_repuesto());
        dto.setPrecio(entity.getPrecio_sugerido());
        dto.setStock(entity.getStock_actual());
        
        // Sistema (nombre o vacío)
        String nombreSistema = entity.getSistema() != null ? entity.getSistema().getNombreSistema() : "";
        dto.setSistema(nombreSistema);
        
        // Compatibilidad resumen
        dto.setCompatibilidadResumen(buildCompatibilidadResumen(entity.getCompatibilidades()));
        
        // Search index (campo calculado)
        dto.setSearchIndex(buildSearchIndex(entity, nombreSistema));
        
        return dto;
    }

    private String buildCompatibilidadResumen(List<CompatibilidadEntity> compatibilidades) {
        if (compatibilidades == null || compatibilidades.isEmpty()) {
            return "";
        }

        // Agrupar por vehículo
        Map<String, List<CompatibilidadEntity>> porVehiculo = compatibilidades.stream()
                .collect(Collectors.groupingBy(c -> 
                    c.getVehiculo().getMarca() + " " + c.getVehiculo().getModelo()
                ));

        // Construir resumen
        return porVehiculo.entrySet().stream()
                .map(entry -> {
                    String vehiculo = entry.getKey();
                    List<CompatibilidadEntity> compats = entry.getValue();
                    
                    // Obtener rango de años
                    String rangoAnios = compats.stream()
                            .map(this::formatRangoAnios)
                            .filter(s -> !s.isEmpty())
                            .collect(Collectors.joining(", "));
                    
                    if (rangoAnios.isEmpty()) {
                        return vehiculo;
                    }
                    return vehiculo + " " + rangoAnios;
                })
                .collect(Collectors.joining(", "));
    }

    private String formatRangoAnios(CompatibilidadEntity comp) {
        if (comp.getAnioInicio() == null) {
            return "";
        }
        
        Integer anioFin = comp.getAnioFin();
        if (anioFin == null) {
            anioFin = Year.now().getValue();
        }
        
        if (comp.getAnioInicio().equals(anioFin)) {
            return String.valueOf(comp.getAnioInicio());
        }
        
        return comp.getAnioInicio() + "-" + anioFin;
    }

    private String buildSearchIndex(RepuestoEntity entity, String nombreSistema) {
        StringBuilder searchIndex = new StringBuilder();
        
        // 1. Nombre del repuesto
        if (entity.getNombre_repuesto() != null) {
            searchIndex.append(entity.getNombre_repuesto().toLowerCase()).append(" ");
        }
        
        // 2. Tags de búsqueda
        if (entity.getTagsBusqueda() != null && !entity.getTagsBusqueda().isEmpty()) {
            searchIndex.append(entity.getTagsBusqueda().toLowerCase()).append(" ");
        }
        
        // 3. Nombre del sistema
        if (!nombreSistema.isEmpty()) {
            searchIndex.append(nombreSistema.toLowerCase()).append(" ");
        }
        
        // 4. Datos de vehículos (marca y modelo)
        if (entity.getCompatibilidades() != null && !entity.getCompatibilidades().isEmpty()) {
            Set<String> vehiculos = entity.getCompatibilidades().stream()
                    .filter(c -> c.getVehiculo() != null)
                    .map(c -> c.getVehiculo().getMarca() + " " + c.getVehiculo().getModelo())
                    .collect(Collectors.toSet());
            
            for (String vehiculo : vehiculos) {
                searchIndex.append(vehiculo.toLowerCase()).append(" ");
            }
            
            // 5. Expansión de años
            for (CompatibilidadEntity comp : entity.getCompatibilidades()) {
                if (comp.getAnioInicio() != null) {
                    Integer anioFin = comp.getAnioFin();
                    if (anioFin == null) {
                        anioFin = Year.now().getValue();
                    }
                    
                    // Generar todos los años en el rango
                    IntStream.rangeClosed(comp.getAnioInicio(), anioFin)
                            .forEach(anio -> searchIndex.append(anio).append(" "));
                }
            }
        }
        
        return searchIndex.toString().trim();
    }

    public List<RepuestoBusquedaDTO> toDTOList(List<RepuestoEntity> entities) {
        return entities.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
