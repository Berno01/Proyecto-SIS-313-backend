package com.sistemastarija.api_repuestos.repuesto.infrastructure.adapter.out.persistance;

import com.sistemastarija.api_repuestos.categoria.infrastructure.adapter.out.persistance.repository.CategoriaRepository;
import com.sistemastarija.api_repuestos.repuesto.application.port.out.RepuestoPersistancePort;
import com.sistemastarija.api_repuestos.repuesto.domain.model.Repuesto;
import com.sistemastarija.api_repuestos.repuesto.infrastructure.adapter.in.dto.RepuestoBusquedaDTO;
import com.sistemastarija.api_repuestos.repuesto.infrastructure.adapter.in.mapper.RepuestoBusquedaMapper;
import com.sistemastarija.api_repuestos.repuesto.infrastructure.adapter.out.persistance.entity.CompatibilidadEntity;
import com.sistemastarija.api_repuestos.repuesto.infrastructure.adapter.out.persistance.entity.VehiculoEntity;
import com.sistemastarija.api_repuestos.repuesto.infrastructure.adapter.out.persistance.mapper.RepuestoPersistanceMapper;
import com.sistemastarija.api_repuestos.repuesto.infrastructure.adapter.out.persistance.entity.RepuestoEntity;
import com.sistemastarija.api_repuestos.repuesto.infrastructure.adapter.out.persistance.repository.RepuestoRepository;
import com.sistemastarija.api_repuestos.repuesto.infrastructure.adapter.out.persistance.repository.SistemaAutoRepository;
import com.sistemastarija.api_repuestos.repuesto.infrastructure.adapter.out.persistance.repository.VehiculoRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RepuestoRepositoryAdapterMysql implements RepuestoPersistancePort {
    private final RepuestoRepository repository;
    private final RepuestoPersistanceMapper mapper;
    private final CategoriaRepository categoriaRepository;
    private final VehiculoRepository vehiculoRepository;
    private final SistemaAutoRepository sistemaAutoRepository;
    private final RepuestoBusquedaMapper busquedaMapper;
    private final EntityManager entityManager;

    @Override
    public Repuesto save(Repuesto repuestoDomain) {
        RepuestoEntity repuestoEntity = mapper.toRepuestoEntity(repuestoDomain);

        // Manejar las compatibilidades manualmente
        if (repuestoDomain.getCompatibilidades() != null && !repuestoDomain.getCompatibilidades().isEmpty()) {
            List<CompatibilidadEntity> compatibilidadesEntities = repuestoDomain.getCompatibilidades().stream()
                    .map(compatibilidad -> {
                        CompatibilidadEntity compatibilidadEntity = new CompatibilidadEntity();
                        compatibilidadEntity.setRepuesto(repuestoEntity);
                        // Usar getReferenceById para evitar SELECT innecesario
                        compatibilidadEntity.setVehiculo(entityManager.getReference(VehiculoEntity.class, compatibilidad.getVehiculoId()));
                        compatibilidadEntity.setAnioInicio(compatibilidad.getAnioInicio());
                        compatibilidadEntity.setAnioFin(compatibilidad.getAnioFin());
                        compatibilidadEntity.setNotas(compatibilidad.getNotas());
                        return compatibilidadEntity;
                    })
                    .collect(Collectors.toList());
            
            repuestoEntity.setCompatibilidades(compatibilidadesEntities);
        } else {
            repuestoEntity.setCompatibilidades(new ArrayList<>());
        }

        RepuestoEntity savedRepuestoEntity = repository.save(repuestoEntity);
        return mapper.toRepuestoDomain(savedRepuestoEntity);
    }

    @Override
    public List<Repuesto> findAll() {
        return mapper.toRepuestoListDomain(repository.findAllByEstadoRepuestoTrue());
    }

    @Override
    public Optional<Repuesto> findById(Integer idRepuesto) {
        return repository.findById(idRepuesto).map(mapper::toRepuestoDomain);
    }

    @Override
    public boolean validateCategorias(List<Integer> idsCategorias) {
        if (idsCategorias == null || idsCategorias.isEmpty()) {
            return true;
        }
        long count = categoriaRepository.countByIdCategoriaIn(idsCategorias);
        return count == idsCategorias.size();
    }

    @Override
    public boolean validateVehiculos(List<Integer> idsVehiculos) {
        if (idsVehiculos == null || idsVehiculos.isEmpty()) {
            return true;
        }
        long count = vehiculoRepository.countByIdVehiculoIn(idsVehiculos);
        return count == idsVehiculos.size();
    }

    @Override
    public boolean validateSistema(Integer idSistema) {
        if (idSistema == null) {
            return true;
        }
        return sistemaAutoRepository.existsById(idSistema);
    }

    @Override
    public List<RepuestoBusquedaDTO> findAllForCatalogoBusqueda() {
        List<RepuestoEntity> repuestos = repository.findAllForCatalogoBusqueda();
        return busquedaMapper.toDTOList(repuestos);
    }
}
