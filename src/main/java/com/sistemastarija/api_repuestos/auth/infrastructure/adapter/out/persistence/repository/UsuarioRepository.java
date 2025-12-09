package com.sistemastarija.api_repuestos.auth.infrastructure.adapter.out.persistence.repository;

import com.sistemastarija.api_repuestos.auth.infrastructure.adapter.out.persistence.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Integer> {
    Optional<UsuarioEntity> findByUsername(String username);
}
