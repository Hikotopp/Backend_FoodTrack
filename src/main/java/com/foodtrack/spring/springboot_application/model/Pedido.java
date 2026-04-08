package com.foodtrack.spring.springboot_application.model;

// Importaciones necesarias
import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "pedidos")  // La tabla se llama "pedidos" en la BD
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDateTime fechaHora;  // Fecha y hora del pedido
    private String estado;            // PENDIENTE, EN PREPARACION, ENTREGADO, CANCELADO
    private Double total;             // Total del pedido

    // @ManyToOne = Muchos pedidos pueden pertenecer a un usuario
    // @JoinColumn = Columna que hace la relacion (llave foranea)
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;  // Usuario que hizo el pedido

    @ManyToOne
    @JoinColumn(name = "mesa_id")
    private Mesa mesa;  // Mesa asociada al pedido

    // @OneToMany = Un pedido tiene muchos detalles
    // mappedBy = El campo en DetallePedido que mapea esta relacion
    // cascade = Si se guarda/elimina el pedido, se guardan/eliminan los detalles
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<DetallePedido> detalles;  // Lista de productos en el pedido

    // @OneToOne = Un pedido tiene una factura
    // @JsonIgnore = Evita recursion infinita al convertir a JSON
    @OneToOne(mappedBy = "pedido")
    @JsonIgnore
    private Factura factura;  // Factura asociada al pedido
}