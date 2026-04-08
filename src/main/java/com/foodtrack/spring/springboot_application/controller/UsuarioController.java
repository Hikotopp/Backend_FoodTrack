package com.foodtrack.spring.springboot_application.controller;

// Importaciones necesarias
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.foodtrack.spring.springboot_application.model.Usuario;
import com.foodtrack.spring.springboot_application.service.UsuarioService;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioService service;  // Servicio de usuarios

    // Endpoint para registrar nuevos usuarios
    @PostMapping("/registro")
    public ResponseEntity<?> registrar(@RequestBody Usuario u) {
        try {
            // Intentar registrar el usuario
            Usuario nuevo = service.registrar(u);
            // Retornar usuario creado con codigo 200 OK
            return ResponseEntity.ok(nuevo);
            
        } catch (RuntimeException e) {
            // Si hay error (usuario ya existe, etc.)
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            // Retornar error con codigo 400 Bad Request
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
}