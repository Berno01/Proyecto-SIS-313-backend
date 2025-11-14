package com.sistemastarija.api_repuestos.venta.infrastructure.adapter.out.persistance.repository;

import com.sistemastarija.api_repuestos.venta.infrastructure.adapter.out.persistance.entity.VentaRepuestoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VentaRepuestoRepository extends JpaRepository<VentaRepuestoEntity, Long> {
}
