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
public class CompatibilidadRequestDTO {
    @JsonProperty("vehiculo_id")
    private Integer vehiculoId;

    @JsonProperty("anio_inicio")
    private Integer anioInicio;

    @JsonProperty("anio_fin")
    private Integer anioFin;

    private String notas;
}
