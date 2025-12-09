package com.sistemastarija.api_repuestos.auth.infrastructure.adapter.in.web.controller;

import com.sistemastarija.api_repuestos.auth.application.port.in.LoginUseCase;
import com.sistemastarija.api_repuestos.auth.infrastructure.adapter.in.web.dto.LoginRequestDTO;
import com.sistemastarija.api_repuestos.auth.infrastructure.adapter.in.web.dto.LoginResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final LoginUseCase loginUseCase;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequest) {
        LoginResponseDTO response = loginUseCase.login(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
