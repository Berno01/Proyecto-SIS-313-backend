package com.sistemastarija.api_repuestos.repuesto.application.port.in;

import com.sistemastarija.api_repuestos.repuesto.infrastructure.adapter.in.dto.RepuestoBusquedaDTO;

import java.util.List;

public interface ObtenerCatalogoBusquedaUseCase {
    List<RepuestoBusquedaDTO> obtenerCatalogo();
}
