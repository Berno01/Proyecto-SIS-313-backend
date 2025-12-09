package com.sistemastarija.api_repuestos.sistemaauto.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SistemaAutoResponseDTO {
    @JsonProperty("id")
    private Integer id;

    @JsonProperty("nombre")
    private String nombre;
}
