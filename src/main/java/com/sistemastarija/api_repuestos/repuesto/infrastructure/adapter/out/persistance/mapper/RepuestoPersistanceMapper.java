package com.sistemastarija.api_repuestos.repuesto.infrastructure.adapter.out.persistance.mapper;

import com.sistemastarija.api_repuestos.categoria.infrastructure.adapter.out.persistance.entity.CategoriaEntity;
import com.sistemastarija.api_repuestos.repuesto.domain.model.Compatibilidad;
import com.sistemastarija.api_repuestos.repuesto.domain.model.Repuesto;
import com.sistemastarija.api_repuestos.repuesto.infrastructure.adapter.out.persistance.entity.CompatibilidadEntity;
import com.sistemastarija.api_repuestos.repuesto.infrastructure.adapter.out.persistance.entity.RepuestoEntity;
import com.sistemastarija.api_repuestos.repuesto.infrastructure.adapter.out.persistance.entity.SistemaAutoEntity;
import com.sistemastarija.api_repuestos.repuesto.infrastructure.adapter.out.persistance.entity.VehiculoEntity;
import com.sistemastarija.api_repuestos.venta.infrastructure.adapter.out.persistance.mapper.DetalleVentaPersistanceMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {DetalleVentaPersistanceMapper.class})
public interface RepuestoPersistanceMapper {
    @Mapping(target = "id_repuesto", source = "idRepuesto")
    @Mapping(target = "nombre_repuesto", source = "nombreRepuesto")
    @Mapping(target = "stock_actual", source = "stockActual")
    @Mapping(target = "costo_repuesto", source = "costoRepuesto")
    @Mapping(target = "precio_sugerido", source = "precioSugerido")
    @Mapping(target = "estadoRepuesto", source = "estadoRepuesto")
    @Mapping(target = "tagsBusqueda", source = "tagsBusqueda")
    @Mapping(target = "sistema", source = "idSistema")
    @Mapping(target = "categorias", source = "idsCategorias")
    @Mapping(target = "compatibilidades", ignore = true) // Se manejará manualmente

    RepuestoEntity toRepuestoEntity(Repuesto repuesto);

    @Mapping(target = "idRepuesto", source = "id_repuesto")
    @Mapping(target = "nombreRepuesto", source = "nombre_repuesto")
    @Mapping(target = "stockActual", source = "stock_actual")
    @Mapping(target = "costoRepuesto", source = "costo_repuesto")
    @Mapping(target = "precioSugerido", source = "precio_sugerido")
    @Mapping(target = "estadoRepuesto", source = "estadoRepuesto")
    @Mapping(target = "tagsBusqueda", source = "tagsBusqueda")
    @Mapping(target = "idSistema", source = "sistema")
    @Mapping(target = "idsCategorias", source = "categorias")
    @Mapping(target = "compatibilidades", source = "compatibilidades")
    Repuesto toRepuestoDomain(RepuestoEntity repuestoEntity);

    List<Repuesto> toRepuestoListDomain(List<RepuestoEntity> repuestoEntities);

    default CategoriaEntity map(Integer idCategoria) {
        if (idCategoria == null) return null;
        CategoriaEntity c = new CategoriaEntity();
        c.setIdCategoria(idCategoria); // solo asignamos el ID
        return c;
    }

    default Integer map(CategoriaEntity categoriaEntity) {
        if (categoriaEntity == null) return null;
        return categoriaEntity.getIdCategoria(); // extraemos solo el ID
    }

    // Mapeo de Sistema
    default SistemaAutoEntity mapSistema(Integer idSistema) {
        if (idSistema == null) return null;
        SistemaAutoEntity sistema = new SistemaAutoEntity();
        sistema.setIdSistema(idSistema);
        return sistema;
    }

    default Integer mapSistema(SistemaAutoEntity sistema) {
        if (sistema == null) return null;
        return sistema.getIdSistema();
    }

    // Mapeo de CompatibilidadEntity a Compatibilidad
    default List<Compatibilidad> mapCompatibilidadesEntityToDomain(List<CompatibilidadEntity> entities) {
        if (entities == null || entities.isEmpty()) return null;
        return entities.stream()
                .map(entity -> new Compatibilidad(
                        entity.getIdCompatibilidad(),
                        entity.getVehiculo() != null ? entity.getVehiculo().getIdVehiculo() : null,
                        entity.getAnioInicio(),
                        entity.getAnioFin(),
                        entity.getNotas()
                ))
                .collect(Collectors.toList());
    }

    // Mapeo de Compatibilidad a CompatibilidadEntity (se manejará en el adaptador)
    default VehiculoEntity mapVehiculoId(Integer vehiculoId) {
        if (vehiculoId == null) return null;
        VehiculoEntity vehiculo = new VehiculoEntity();
        vehiculo.setIdVehiculo(vehiculoId);
        return vehiculo;
    }
}
