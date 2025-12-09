package com.sistemastarija.api_repuestos.auth;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Utilidad para generar hashes BCrypt de contraseñas
 * Este archivo NO es parte del código productivo, es solo para generar hashes
 */
public class PasswordHashGenerator {
    
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        // Generar hash para la contraseña 'admin123'
        String password = "admin123";
        String hash = encoder.encode(password);
        
        System.out.println("Password: " + password);
        System.out.println("BCrypt Hash: " + hash);
        
        // Verificar que el hash funciona correctamente
        boolean matches = encoder.matches(password, hash);
        System.out.println("Verificación: " + (matches ? "OK" : "FAILED"));
        
        // Ejemplo de hash para 'admin123' (este es el usado en el SQL):
        String predefinedHash = "$2a$10$8K1p/a0dL1H3h8MwMvTtFOazLCq7HVMTzL8JJMzQyXKaVCv4Y2Hry";
        boolean predefinedMatches = encoder.matches(password, predefinedHash);
        System.out.println("\nVerificación con hash predefinido: " + (predefinedMatches ? "OK" : "FAILED"));
    }
}
