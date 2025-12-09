package com.sistemastarija.api_repuestos.auth.infrastructure.adapter.out.persistence.mapper;

import com.sistemastarija.api_repuestos.auth.domain.model.Usuario;
import com.sistemastarija.api_repuestos.auth.infrastructure.adapter.out.persistence.entity.UsuarioEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioPersistenceMapper {
    
    @Mapping(source = "id_usuario", target = "idUsuario")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "nombre_completo", target = "nombreCompleto")
    @Mapping(source = "rol", target = "rol")
    Usuario toUsuarioModel(UsuarioEntity entity);
    
    @InheritInverseConfiguration
    UsuarioEntity toUsuarioEntity(Usuario usuario);
}
