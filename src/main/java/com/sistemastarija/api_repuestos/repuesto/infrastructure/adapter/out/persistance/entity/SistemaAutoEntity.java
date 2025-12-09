package com.sistemastarija.api_repuestos.repuesto.infrastructure.adapter.out.persistance.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sistema_auto")
public class SistemaAutoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sistema")
    private Integer idSistema;

    @Column(name = "nombre_sistema", nullable = false, length = 100)
    private String nombreSistema;
}
