package com.foodtrack.spring.springboot_application.model;

// Importaciones necesarias
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer cantidad;               // Cantidad disponible
    private LocalDateTime fechaActualizacion; // Ultima fecha de actualizacion

    // Relacion uno a uno con Producto
    @OneToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;
}