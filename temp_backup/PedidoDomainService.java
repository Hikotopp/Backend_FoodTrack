package com.foodtrack.spring.springboot_application.domain.service;

import com.foodtrack.spring.springboot_application.domain.model.Pedido;
import com.foodtrack.spring.springboot_application.domain.port.in.PedidoUseCase;
import com.foodtrack.spring.springboot_application.domain.port.out.PedidoRepositoryPort;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoDomainService implements PedidoUseCase {

    private final PedidoRepositoryPort pedidoRepositoryPort;

    public PedidoDomainService(PedidoRepositoryPort pedidoRepositoryPort) {
        this.pedidoRepositoryPort = pedidoRepositoryPort;
    }

    @Override
    public List<Pedido> listar() {
        return pedidoRepositoryPort.findAll();
    }

    @Override
    public Optional<Pedido> buscarPorId(Long id) {
        return pedidoRepositoryPort.findById(id);
    }

    @Override
    public List<Pedido> buscarPorMesaId(Long mesaId) {
        return pedidoRepositoryPort.findByMesaId(mesaId);
    }

    @Override
    public List<Pedido> buscarPorUsuarioId(Long usuarioId) {
        return pedidoRepositoryPort.findByUsuarioId(usuarioId);
    }

    @Override
    public Pedido guardar(Pedido pedido) {
        pedido.setFechaHora(LocalDateTime.now());
        pedido.setEstado("PENDIENTE");
        return pedidoRepositoryPort.save(pedido);
    }

    @Override
    public Pedido actualizarEstado(Long id, String estado) {
        Pedido pedido = pedidoRepositoryPort.findById(id)
            .orElseThrow(() -> new RuntimeException("Pedido no encontrado con ID: " + id));
        pedido.setEstado(estado);
        return pedidoRepositoryPort.save(pedido);
    }

    @Override
    public void eliminar(Long id) {
        pedidoRepositoryPort.deleteById(id);
    }
}