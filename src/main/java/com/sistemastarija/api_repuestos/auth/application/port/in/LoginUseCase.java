package com.sistemastarija.api_repuestos.auth.application.port.in;

import com.sistemastarija.api_repuestos.auth.infrastructure.adapter.in.web.dto.LoginResponseDTO;

public interface LoginUseCase {
    LoginResponseDTO login(String username, String password);
}
