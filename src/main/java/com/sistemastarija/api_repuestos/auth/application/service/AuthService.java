package com.sistemastarija.api_repuestos.auth.application.service;

import com.sistemastarija.api_repuestos.auth.application.port.in.LoginUseCase;
import com.sistemastarija.api_repuestos.auth.application.port.out.UsuarioPersistencePort;
import com.sistemastarija.api_repuestos.auth.domain.exception.InvalidCredentialsException;
import com.sistemastarija.api_repuestos.auth.domain.exception.UsuarioNotFoundException;
import com.sistemastarija.api_repuestos.auth.domain.model.Usuario;
import com.sistemastarija.api_repuestos.auth.infrastructure.adapter.in.web.dto.LoginResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService implements LoginUseCase {

    private final UsuarioPersistencePort usuarioPersistencePort;
    private final PasswordEncoder passwordEncoder;

    @Override
    public LoginResponseDTO login(String username, String password) {
        // 1. Buscar usuario por username
        Usuario usuario = usuarioPersistencePort.findByUsername(username)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuario no encontrado: " + username));

        // 2. Verificar la contraseña usando BCrypt
        if (!passwordEncoder.matches(password, usuario.getPassword())) {
            throw new InvalidCredentialsException("Credenciales inválidas");
        }

        // 3. Mapear a DTO sin exponer la contraseña
        return new LoginResponseDTO(
                usuario.getIdUsuario(),
                usuario.getUsername(),
                usuario.getNombreCompleto(),
                usuario.getRol()
        );
    }
}
