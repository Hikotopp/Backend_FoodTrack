package com.foodtrack.spring.springboot_application.model;

// Importaciones necesarias
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity
@Data
@NoArgsConstructor  // Constructor sin argumentos
@AllArgsConstructor // Constructor con todos los argumentos
@Builder  // Patron builder para crear objetos facilmente
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private String nombre;      // Nombre del producto
    private Double precio;      // Precio del producto
    private String categoria;   // Categoria (Ej: Comida Rapida, Italiana, etc.)
    private Boolean disponible; // Si esta disponible para la venta
}