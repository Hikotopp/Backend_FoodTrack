package com.foodtrack.spring.springboot_application.model;

// Importaciones necesarias
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Double total;           // Total de la factura
    private String metodoPago;      // EFECTIVO, TARJETA, QR
    private LocalDateTime fechaHora; // Fecha y hora de emision

    // Relacion uno a uno con Pedido
    @OneToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;
}