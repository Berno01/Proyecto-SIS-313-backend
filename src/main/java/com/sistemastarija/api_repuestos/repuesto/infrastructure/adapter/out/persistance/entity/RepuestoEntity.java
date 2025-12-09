package com.sistemastarija.api_repuestos.repuesto.infrastructure.adapter.out.persistance.entity;

import com.sistemastarija.api_repuestos.categoria.infrastructure.adapter.out.persistance.entity.CategoriaEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "repuesto")
public class RepuestoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_repuesto;

    private String nombre_repuesto;
    private Integer stock_actual;
    private Double costo_repuesto;
    private Double precio_sugerido;
    private Boolean estadoRepuesto;

    @Column(name = "tags_busqueda", columnDefinition = "TEXT")
    private String tagsBusqueda;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_sistema")
    private SistemaAutoEntity sistema;

    @ManyToMany
    @JoinTable(
            name = "repuesto_categoria",
            joinColumns = @JoinColumn(name = "id_repuesto"),
            inverseJoinColumns = @JoinColumn(name = "id_categoria")
    )
    private List<CategoriaEntity> categorias;

    @OneToMany(mappedBy = "repuesto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CompatibilidadEntity> compatibilidades = new ArrayList<>();
}
