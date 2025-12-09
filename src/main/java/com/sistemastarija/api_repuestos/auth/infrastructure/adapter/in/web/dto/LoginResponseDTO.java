package com.sistemastarija.api_repuestos.auth.infrastructure.adapter.in.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
    @JsonProperty("id_usuario")
    private Integer idUsuario;
    
    @JsonProperty("username")
    private String username;
    
    @JsonProperty("nombre_completo")
    private String nombreCompleto;
    
    @JsonProperty("rol")
    private String rol;
}
