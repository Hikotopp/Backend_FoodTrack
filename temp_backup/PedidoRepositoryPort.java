package com.foodtrack.spring.springboot_application.domain.port.out;

import com.foodtrack.spring.springboot_application.domain.model.Pedido;
import java.util.List;
import java.util.Optional;

public interface PedidoRepositoryPort {
    List<Pedido> findAll();
    Optional<Pedido> findById(Long id);
    List<Pedido> findByMesaId(Long mesaId);
    List<Pedido> findByUsuarioId(Long usuarioId);
    Pedido save(Pedido pedido);
    void deleteById(Long id);
}