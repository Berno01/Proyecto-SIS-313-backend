package com.sistemastarija.api_repuestos.venta.infrastructure.adapter.out.persistance.repository;

import com.sistemastarija.api_repuestos.venta.infrastructure.adapter.out.persistance.entity.VentaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VentaRepository extends JpaRepository<VentaEntity, Long> {
    
    @Query("SELECT v FROM VentaEntity v WHERE v.estadoVenta = true ORDER BY v.id_venta DESC")
    List<VentaEntity> findAllByEstadoVentaTrue();
}
