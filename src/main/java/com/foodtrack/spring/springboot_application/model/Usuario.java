package com.foodtrack.spring.springboot_application.model;

// Importaciones necesarias
import jakarta.persistence.*;
import lombok.Data;

// @Entity = Esta clase es una entidad de base de datos
// @Data = Lombok - Genera getters, setters, toString, equals, hashCode
@Entity
@Data
// @Table = Define el nombre de la tabla en la base de datos
@Table(name = "usuarios")
public class Usuario {

    // @Id = Este campo es la llave primaria
    // @GeneratedValue = Se genera automaticamente (auto-increment)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;  // Nombre completo del usuario
    
    // @Column(unique = true) = No pueden haber dos usuarios con el mismo nombre de usuario
    @Column(unique = true)
    private String usuario;  // Nombre de usuario para login
    
    private String contrasena;  // Contrasena (en produccion deberia estar encriptada)
    
    private String tipoUsuario;  // ADMIN o CLIENTE
}