package com.sistemastarija.api_repuestos.auth.application.port.out;

import com.sistemastarija.api_repuestos.auth.domain.model.Usuario;

import java.util.Optional;

public interface UsuarioPersistencePort {
    Optional<Usuario> findByUsername(String username);
}
