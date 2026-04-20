package com.foodtrack.spring.springboot_application.infrastructure.persistence;

import com.foodtrack.spring.springboot_application.infrastructure.persistence.entity.PedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaPedidoRepository extends JpaRepository<PedidoEntity, Long> {
    List<PedidoEntity> findByMesaId(Long mesaId);
    List<PedidoEntity> findByUsuarioId(Long usuarioId);
}
