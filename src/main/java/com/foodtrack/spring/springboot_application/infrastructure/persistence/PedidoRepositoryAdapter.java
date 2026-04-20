package com.foodtrack.spring.springboot_application.infrastructure.persistence;

import com.foodtrack.spring.springboot_application.domain.model.Pedido;
import com.foodtrack.spring.springboot_application.domain.port.out.PedidoRepositoryPort;
import com.foodtrack.spring.springboot_application.infrastructure.persistence.entity.PedidoEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Objects;
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
        Objects.requireNonNull(id, "El id no puede ser nulo");
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
        PedidoEntity entity = Objects.requireNonNull(toEntity(pedido), "Entity must not be null");
        PedidoEntity savedEntity = Objects.requireNonNull(jpaPedidoRepository.save(entity), "Saved entity must not be null");
        return toDomain(savedEntity);
    }

    @Override
    public void deleteById(Long id) {
        Objects.requireNonNull(id, "El id no puede ser nulo");
        jpaPedidoRepository.deleteById(id);
    }

    private Pedido toDomain(PedidoEntity entity) {
        Objects.requireNonNull(entity, "Entity must not be null");
        Pedido pedido = new Pedido();
        Long id = entity.getId();
        if (id != null) {
            pedido.setId(id);
        }
        pedido.setMesaId(entity.getMesaId());
        pedido.setUsuarioId(entity.getUsuarioId());
        pedido.setFechaHora(entity.getFechaHora());
        pedido.setEstado(entity.getEstado());
        pedido.setTotal(entity.getTotal());
        return pedido;
    }

    private PedidoEntity toEntity(Pedido domain) {
        PedidoEntity entity = new PedidoEntity();
        Long id = domain.getId();
        if (id != null) {
            entity.setId(id);
        }
        entity.setMesaId(domain.getMesaId());
        entity.setUsuarioId(domain.getUsuarioId());
        entity.setFechaHora(domain.getFechaHora());
        entity.setEstado(domain.getEstado());
        entity.setTotal(domain.getTotal());
        return entity;
    }
}
