package com.sistemastarija.api_repuestos.common.audit;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Clase base abstracta para auditoría de entidades.
 * Proporciona campos comunes para rastrear la creación y actualización de registros.
 * 
 * Las clases que extiendan esta clase heredarán automáticamente los campos de auditoría.
 */
@Getter
@Setter
@MappedSuperclass
public abstract class AuditEntity {

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by", updatable = false)
    private Integer createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private Integer updatedBy;
}
