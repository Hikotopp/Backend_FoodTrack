package com.foodtrack.spring.springboot_application.security;

// Importaciones necesarias
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

// Filtro que intercepta todas las peticiones para validar el JWT
// OncePerRequestFilter = Se ejecuta una sola vez por peticion
@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;  // Utilidad para JWT

    // Metodo principal del filtro
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain chain)
            throws ServletException, IOException {

        // Obtener la ruta de la peticion
        String path = request.getRequestURI();
        
        // Rutas publicas - no requieren autenticacion
        // Estas rutas son accesibles sin token
        if (path.startsWith("/auth/login") || path.startsWith("/usuarios/registro")) {
            // Continuar con la peticion sin validar token
            chain.doFilter(request, response);
            return;
        }

        // Obtener el header Authorization
        String authHeader = request.getHeader("Authorization");

        // Verificar si el header existe y tiene el formato correcto
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // Extraer el token (quitar "Bearer ")
            String token = authHeader.substring(7);
            
            // Validar el token
            if (jwtUtil.validateToken(token)) {
                // Token valido - continuar con la peticion
                chain.doFilter(request, response);
                return;
            }
        }

        // Si llegamos aqui, no hay token o es invalido
        // Retornar error 401 Unauthorized
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\":\"Token no valido o no proporcionado\"}");
    }
}