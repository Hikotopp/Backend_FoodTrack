package com.foodtrack.spring.springboot_application.repository;

// Importaciones necesarias
import org.springframework.data.jpa.repository.JpaRepository;
import com.foodtrack.spring.springboot_application.model.Usuario;

// JpaRepository<Entidad, TipoDeId> - Proporciona metodos CRUD basicos
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    
    // Metodo personalizado para buscar un usuario por su nombre de usuario
    // Spring Data JPA implementa automaticamente este metodo
    Usuario findByUsuario(String usuario);
}