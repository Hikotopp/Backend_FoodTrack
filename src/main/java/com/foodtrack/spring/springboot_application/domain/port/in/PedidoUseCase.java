package com.foodtrack.spring.springboot_application.domain.port.in;

import com.foodtrack.spring.springboot_application.domain.model.Pedido;
import java.util.List;
import java.util.Optional;

public interface PedidoUseCase {
    List<Pedido> listar();
    Optional<Pedido> buscarPorId(Long id);
    List<Pedido> buscarPorMesaId(Long mesaId);
    List<Pedido> buscarPorUsuarioId(Long usuarioId);
    Pedido guardar(Pedido pedido);
    Pedido actualizarEstado(Long id, String estado);
    void eliminar(Long id);
}
