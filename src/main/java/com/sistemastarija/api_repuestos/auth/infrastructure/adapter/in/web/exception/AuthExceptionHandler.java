package com.sistemastarija.api_repuestos.auth.infrastructure.adapter.in.web.exception;

import com.sistemastarija.api_repuestos.auth.domain.exception.InvalidCredentialsException;
import com.sistemastarija.api_repuestos.auth.domain.exception.UsuarioNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class AuthExceptionHandler {

    @ExceptionHandler(UsuarioNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUsuarioNotFoundException(UsuarioNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Usuario no encontrado");
        error.put("mensaje", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleInvalidCredentialsException(InvalidCredentialsException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Credenciales inválidas");
        error.put("mensaje", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }
}
