package com.sistemastarija.api_repuestos.repuesto.infrastructure.adapter.in.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RepuestoBusquedaDTO {
    private Integer id;
    private String nombre;
    private Double precio;
    private Integer stock;
    private String sistema;
    
    @JsonProperty("compatibilidad_resumen")
    private String compatibilidadResumen;
    
    @JsonProperty("search_index")
    private String searchIndex;
}
