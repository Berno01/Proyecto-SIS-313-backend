package com.sistemastarija.api_repuestos.repuesto.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Compatibilidad {
    private Integer id;
    private Integer vehiculoId;
    private Integer anioInicio;
    private Integer anioFin;
    private String notas;
}
