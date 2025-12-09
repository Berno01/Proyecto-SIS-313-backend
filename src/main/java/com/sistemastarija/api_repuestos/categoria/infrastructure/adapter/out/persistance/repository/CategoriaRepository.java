package com.sistemastarija.api_repuestos.categoria.infrastructure.adapter.out.persistance.repository;

import com.sistemastarija.api_repuestos.categoria.infrastructure.adapter.out.persistance.entity.CategoriaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<CategoriaEntity, Long> {
    List<CategoriaEntity> findAllByEstadoCategoriaTrue();

    boolean countByIdCategoriaIn(Collection<Integer> idCategorias);

    long countByIdCategoriaIn(List<Integer> idCategorias);
    
    @Query("SELECT c FROM CategoriaEntity c WHERE c.nombre_categoria = :nombreCategoria")
    Optional<CategoriaEntity> findByNombreCategoria(@Param("nombreCategoria") String nombreCategoria);
}
