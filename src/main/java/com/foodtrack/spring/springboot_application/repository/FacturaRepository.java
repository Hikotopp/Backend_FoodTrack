package com.foodtrack.spring.springboot_application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.foodtrack.spring.springboot_application.model.Factura;
import java.util.Optional;

public interface FacturaRepository extends JpaRepository<Factura, Integer> {
    // Buscar factura por ID de pedido
    // Optional = Puede o no existir (evita NullPointerException)
    Optional<Factura> findByPedidoId(Integer pedidoId);
}