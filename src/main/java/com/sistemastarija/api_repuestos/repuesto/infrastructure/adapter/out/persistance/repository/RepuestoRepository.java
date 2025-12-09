package com.sistemastarija.api_repuestos.repuesto.infrastructure.adapter.out.persistance.repository;

import com.sistemastarija.api_repuestos.repuesto.infrastructure.adapter.out.persistance.entity.RepuestoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RepuestoRepository extends JpaRepository<RepuestoEntity, Integer> {

    List<RepuestoEntity> findAllByEstadoRepuestoTrue();

    @Query("SELECT DISTINCT r FROM RepuestoEntity r " +
           "LEFT JOIN FETCH r.sistema " +
           "LEFT JOIN FETCH r.compatibilidades c " +
           "LEFT JOIN FETCH c.vehiculo " +
           "WHERE r.estadoRepuesto = true")
    List<RepuestoEntity> findAllForCatalogoBusqueda();
}
