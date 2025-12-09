package com.sistemastarija.api_repuestos.repuesto.infrastructure.adapter.out.persistance.repository;

import com.sistemastarija.api_repuestos.repuesto.infrastructure.adapter.out.persistance.entity.SistemaAutoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SistemaAutoRepository extends JpaRepository<SistemaAutoEntity, Integer> {
    Optional<SistemaAutoEntity> findByNombreSistema(String nombreSistema);
}
