package com.sistemastarija.api_repuestos.auth.infrastructure.adapter.out.persistence;

import com.sistemastarija.api_repuestos.auth.application.port.out.UsuarioPersistencePort;
import com.sistemastarija.api_repuestos.auth.domain.model.Usuario;
import com.sistemastarija.api_repuestos.auth.infrastructure.adapter.out.persistence.mapper.UsuarioPersistenceMapper;
import com.sistemastarija.api_repuestos.auth.infrastructure.adapter.out.persistence.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UsuarioRepositoryAdapter implements UsuarioPersistencePort {

    private final UsuarioRepository repository;
    private final UsuarioPersistenceMapper mapper;

    @Override
    public Optional<Usuario> findByUsername(String username) {
        return repository.findByUsername(username)
                .map(mapper::toUsuarioModel);
    }
}
