package com.foodtrack.spring.springboot_application.service;

import com.foodtrack.spring.springboot_application.model.DetallePedido;
import com.foodtrack.spring.springboot_application.repository.DetallePedidoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Slf4j
public class DetallePedidoService {

    @Autowired
    private DetallePedidoRepository repo;

    public List<DetallePedido> listar() {
        log.info("Listando detalles de pedidos");
        return repo.findAll();
    }

    public DetallePedido guardar(@NonNull DetallePedido detalle) {
        log.info("Guardando detalle de pedido");
        return repo.save(detalle);
    }
}