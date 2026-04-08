package com.foodtrack.spring.springboot_application.controller;

// Importaciones necesarias
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.foodtrack.spring.springboot_application.model.Usuario;
import com.foodtrack.spring.springboot_application.security.JwtUtil;
import com.foodtrack.spring.springboot_application.service.UsuarioService;
import java.util.HashMap;
import java.util.Map;

// @RestController = Define esta clase como controlador REST
// @RequestMapping = Ruta base para todos los endpoints de este controlador
@RestController
@RequestMapping("/auth")
// @CrossOrigin = Permite peticiones desde otros dominios (CORS)
@CrossOrigin(origins = "*")
public class AuthController {

    // Inyeccion de dependencias - Spring crea automaticamente las instancias
    @Autowired
    private UsuarioService service;  // Servicio de usuarios

    @Autowired
    private JwtUtil jwt;  // Utilidad para JWT

    // Endpoint para login de usuarios
    // @PostMapping = Maneja peticiones HTTP POST
    // @RequestBody = Convierte el JSON de la peticion a objeto Usuario
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario u) {
        try {
            // Intentar hacer login con usuario y contrasena
            Usuario user = service.login(u.getUsuario(), u.getContrasena());
            
            // Generar token JWT para el usuario autenticado
            String token = jwt.generateToken(user.getUsuario());
            
            // Crear respuesta con los datos del usuario y el token
            Map<String, String> response = new HashMap<>();
            response.put("token", token);  // Token JWT
            response.put("tipoUsuario", user.getTipoUsuario());  // ADMIN o CLIENTE
            response.put("nombre", user.getNombre());  // Nombre del usuario
            response.put("usuario", user.getUsuario());  // Nombre de usuario
            
            // Retornar respuesta exitosa con codigo 200 OK
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            // Si hay error, crear respuesta de error
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());  // Mensaje de error
            
            // Retornar error con codigo 401 Unauthorized
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }
}