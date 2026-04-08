package com.foodtrack.spring.springboot_application.service;

// Importaciones necesarias
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.foodtrack.spring.springboot_application.model.Usuario;
import com.foodtrack.spring.springboot_application.repository.UsuarioRepository;

// @Service = Define esta clase como un servicio (logica de negocio)
// @Slf4j = Lombok - Proporciona un logger para registrar eventos
@Service
@Slf4j
public class UsuarioService {

    // Inyeccion del repositorio
    @Autowired
    private UsuarioRepository repo;

    // Metodo para registrar un nuevo usuario
    public Usuario registrar(Usuario u) {
        // Log de informacion
        log.info("Registrando usuario: {}", u.getUsuario());
        
        // Validacion: usuario no puede estar vacio
        if (u.getUsuario() == null || u.getUsuario().trim().isEmpty()) {
            throw new RuntimeException("El nombre de usuario no puede estar vacio");
        }
        
        // Validacion: verificar si el usuario ya existe
        if (repo.findByUsuario(u.getUsuario()) != null) {
            throw new RuntimeException("Usuario ya existe");
        }
        
        // Validacion: contrasena no puede estar vacia
        if (u.getContrasena() == null || u.getContrasena().trim().isEmpty()) {
            throw new RuntimeException("La contrasena no puede estar vacia");
        }
        
        // Si no se proporciona nombre, usar el nombre de usuario
        if (u.getNombre() == null || u.getNombre().trim().isEmpty()) {
            u.setNombre(u.getUsuario());
        }
        
        // Si no se proporciona tipo, asignar CLIENTE por defecto
        if (u.getTipoUsuario() == null || u.getTipoUsuario().trim().isEmpty()) {
            u.setTipoUsuario("CLIENTE");
        }
        
        // Guardar y retornar el usuario
        return repo.save(u);
    }

    // Metodo para login
    public Usuario login(String usuario, String contrasena) {
        log.info("Login intento: {}", usuario);
        
        // Validacion de parametros
        if (usuario == null || contrasena == null) {
            throw new RuntimeException("Usuario y contrasena son requeridos");
        }
        
        // Buscar usuario por nombre de usuario
        Usuario u = repo.findByUsuario(usuario);
        
        // Verificar si existe
        if (u == null) {
            throw new RuntimeException("Usuario no existe");
        }
        
        // Verificar contrasena (comparacion directa - en produccion usar encriptacion)
        if (!contrasena.equals(u.getContrasena())) {
            throw new RuntimeException("Contrasena incorrecta");
        }
        
        return u;
    }
}