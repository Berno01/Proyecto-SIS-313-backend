package com.sistemastarija.api_repuestos.repuesto.infrastructure.adapter.out.persistance.repository;

import com.sistemastarija.api_repuestos.repuesto.infrastructure.adapter.out.persistance.entity.VehiculoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehiculoRepository extends JpaRepository<VehiculoEntity, Integer> {
    long countByIdVehiculoIn(List<Integer> ids);
    
    Optional<VehiculoEntity> findByMarcaAndModelo(String marca, String modelo);
}
