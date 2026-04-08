package com.foodtrack.spring.springboot_application.service;

import com.foodtrack.spring.springboot_application.model.Pedido;
import com.foodtrack.spring.springboot_application.repository.PedidoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class PedidoService {

    @Autowired
    private PedidoRepository repo;

    public List<Pedido> listar() {
        log.info("Listando todos los pedidos");
        return repo.findAll();
    }

    public Pedido obtenerPorId(@NonNull Integer id) {
        log.info("Buscando pedido ID: {}", id);
        return repo.findById(id)
            .orElseThrow(() -> new RuntimeException("Pedido no encontrado con ID: " + id));
    }

    public Pedido crearPedido(@NonNull Pedido pedido) {
        log.info("Creando nuevo pedido");
        pedido.setFechaHora(LocalDateTime.now());
        pedido.setEstado("PENDIENTE");
        pedido.setTotal(0.0);
        return repo.save(pedido);
    }

    public Pedido actualizar(@NonNull Integer id, @NonNull Pedido pedido) {
        log.info("Actualizando pedido ID: {}", id);
        Pedido existente = obtenerPorId(id);
        existente.setEstado(pedido.getEstado());
        existente.setTotal(pedido.getTotal());
        return repo.save(existente);
    }

    public void eliminar(@NonNull Integer id) {
        log.info("Eliminando pedido ID: {}", id);
        repo.deleteById(id);
    }

    public Pedido cambiarEstado(@NonNull Integer id, @NonNull String estado) {
        log.info("Cambiando estado del pedido {} a {}", id, estado);
        Pedido pedido = obtenerPorId(id);
        pedido.setEstado(estado);
        return repo.save(pedido);
    }
}