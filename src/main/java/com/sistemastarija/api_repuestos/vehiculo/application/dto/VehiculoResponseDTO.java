package com.sistemastarija.api_repuestos.vehiculo.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VehiculoResponseDTO {
    @JsonProperty("id")
    private Integer id;

    @JsonProperty("marca")
    private String marca;

    @JsonProperty("modelo")
    private String modelo;
}
