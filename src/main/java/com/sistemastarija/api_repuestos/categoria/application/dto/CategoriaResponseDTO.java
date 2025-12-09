package com.sistemastarija.api_repuestos.categoria.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoriaResponseDTO {
    @JsonProperty("id")
    private Integer id;

    @JsonProperty("nombre")
    private String nombre;
}
