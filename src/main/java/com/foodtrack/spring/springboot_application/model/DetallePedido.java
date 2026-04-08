package com.foodtrack.spring.springboot_application.model;

// Importaciones necesarias
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class DetallePedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer cantidad;   // Cantidad de productos
    private Double subtotal;    // Precio * cantidad

    // Relacion con Pedido
    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    // Relacion con Producto
    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;
}