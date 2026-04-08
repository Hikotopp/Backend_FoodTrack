package com.foodtrack.spring.springboot_application.service;

import com.foodtrack.spring.springboot_application.model.Factura;
import com.foodtrack.spring.springboot_application.repository.FacturaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class FacturaService {

    @Autowired
    private FacturaRepository repo;

    public List<Factura> listar() {
        log.info("Listando todas las facturas");
        return repo.findAll();
    }

    public Factura obtenerPorId(@NonNull Integer id) {
        log.info("Buscando factura ID: {}", id);
        return repo.findById(id)
            .orElseThrow(() -> new RuntimeException("Factura no encontrada con ID: " + id));
    }

    public Factura obtenerPorPedido(@NonNull Integer pedidoId) {
        log.info("Buscando factura del pedido ID: {}", pedidoId);
        return repo.findByPedidoId(pedidoId)
            .orElseThrow(() -> new RuntimeException("Factura no encontrada para pedido ID: " + pedidoId));
    }

    public Factura generarFactura(@NonNull Factura factura) {
        log.info("Generando factura");
        factura.setFechaHora(LocalDateTime.now());
        return repo.save(factura);
    }
}