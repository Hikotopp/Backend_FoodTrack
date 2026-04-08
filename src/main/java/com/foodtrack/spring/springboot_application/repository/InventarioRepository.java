package com.foodtrack.spring.springboot_application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.foodtrack.spring.springboot_application.model.Inventario;

public interface InventarioRepository extends JpaRepository<Inventario, Integer> {
}