package com.foodtrack.spring.springboot_application.model;

// Importaciones necesarias
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Data
public class Mesa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer numero;  // Numero de la mesa (1, 2, 3, etc.)
    private String estado;   // LIBRE, OCUPADA, RESERVADA

    // Una mesa puede tener muchos pedidos
    @OneToMany(mappedBy = "mesa")
    private List<Pedido> pedidos;
}