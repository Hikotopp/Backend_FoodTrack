package com.foodtrack.spring.springboot_application.infrastructure.persistence;

import com.foodtrack.spring.springboot_application.infrastructure.persistence.entity.DetallePedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaDetallePedidoRepository extends JpaRepository<DetallePedidoEntity, Long> {
    List<DetallePedidoEntity> findByPedidoId(Long pedidoId);
}
