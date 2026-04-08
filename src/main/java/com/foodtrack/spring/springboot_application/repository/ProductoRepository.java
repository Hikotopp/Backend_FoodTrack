package com.foodtrack.spring.springboot_application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.foodtrack.spring.springboot_application.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    // Hereda metodos: findAll(), findById(), save(), deleteById(), etc.
}