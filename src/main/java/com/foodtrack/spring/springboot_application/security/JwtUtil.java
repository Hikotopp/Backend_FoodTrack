package com.foodtrack.spring.springboot_application.security;

// Importaciones necesarias
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

// Utilidad para manejar JWT (JSON Web Tokens)
@Component  // Permite inyectar esta clase en otros componentes
public class JwtUtil {
    
    // Clave secreta para firmar los tokens (generada automaticamente)
    // HS256 = Algoritmo de firma
    // PON ESTA EN SU LUGAR
private final Key SECRET_KEY = Keys.hmacShaKeyFor(
    "foodtrack-clave-secreta-super-segura-2024-abc123xyz".getBytes()
);


    // Genera un token JWT para un usuario
    // @param usuario - Nombre del usuario autenticado
    // @return Token JWT como String
    public String generateToken(String usuario) {
        return Jwts.builder()
                .setSubject(usuario)  // Sujeto del token (el usuario)
                .setIssuedAt(new Date())  // Fecha de emision
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))  // Expira en 24 horas
                .signWith(SECRET_KEY)  // Firmar con la clave secreta
                .compact();  // Generar el token
    }

    // Extrae el nombre de usuario de un token
    public String extractUsuario(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)  // Usar la misma clave para validar
                .build()
                .parseClaimsJws(token)  // Parsear el token
                .getBody()
                .getSubject();  // Obtener el sujeto (usuario)
    }

    // Valida si un token es correcto y no ha expirado
    public boolean validateToken(String token) {
        try {
            // Intentar parsear el token - si falla, es invalido
            Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;  // Token invalido o expirado
        }
    }
}