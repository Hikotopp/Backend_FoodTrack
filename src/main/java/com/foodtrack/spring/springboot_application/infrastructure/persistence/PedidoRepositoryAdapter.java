package com.foodtrack.spring.springboot_application.infrastructure.persistence;

import com.foodtrack.spring.springboot_application.domain.model.Pedido;
import com.foodtrack.spring.springboot_application.domain.port.out.PedidoRepositoryPort;
import com.foodtrack.spring.springboot_application.infrastructure.persistence.entity.PedidoEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class PedidoRepositoryAdapter implements PedidoRepositoryPort {

    private final JpaPedidoRepository jpaPedidoRepository;

    public PedidoRepositoryAdapter(JpaPedidoRepository jpaPedidoRepository) {
        this.jpaPedidoRepository = jpaPedidoRepository;
    }

    @Override
    public List<Pedido> findAll() {
        return jpaPedidoRepository.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Pedido> findById(Long id) {
        return jpaPedidoRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Pedido> findByMesaId(Long mesaId) {
        return jpaPedidoRepository.findByMesaId(mesaId).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Pedido> findByUsuarioId(Long usuarioId) {
        return jpaPedidoRepository.findByUsuarioId(usuarioId).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Pedido save(Pedido pedido) {
        PedidoEntity entity = toEntity(pedido);
        return toDomain(jpaPedidoRepository.save(entity));
    }

    @Override
    public void deleteById(Long id) {
        jpaPedidoRepository.deleteById(id);
    }

    private Pedido toDomain(PedidoEntity entity) {
        Pedido pedido = new Pedido();
        pedido.setId(entity.getId());
        pedido.setMesaId(entity.getMesaId());
        pedido.setUsuarioId(entity.getUsuarioId());
        pedido.setFechaHora(entity.getFechaHora());
        pedido.setEstado(entity.getEstado());
        pedido.setTotal(entity.getTotal());
        return pedido;
    }

    private PedidoEntity toEntity(Pedido domain) {
        PedidoEntity entity = new PedidoEntity();
        entity.setId(domain.getId());
        entity.setMesaId(domain.getMesaId());
        entity.setUsuarioId(domain.getUsuarioId());
        entity.setFechaHora(domain.getFechaHora());
        entity.setEstado(domain.getEstado());
        entity.setTotal(domain.getTotal());
        return entity;
    }
}
