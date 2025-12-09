package com.sistemastarija.api_repuestos.auth.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    private Integer idUsuario;
    private String username;
    private String password;
    private String nombreCompleto;
    private String rol;
}
