package com.foodtrack.spring.springboot_application.service;

import com.foodtrack.spring.springboot_application.model.Inventario;
import com.foodtrack.spring.springboot_application.repository.InventarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class InventarioService {

    @Autowired
    private InventarioRepository repo;

    public List<Inventario> listar() {
        log.info("Listando inventario completo");
        return repo.findAll();
    }

    public Inventario obtenerPorId(@NonNull Integer id) {
        log.info("Buscando registro de inventario ID: {}", id);
        return repo.findById(id)
            .orElseThrow(() -> new RuntimeException("Inventario no encontrado con ID: " + id));
    }

    public Inventario guardar(@NonNull Inventario inventario) {
        log.info("Actualizando inventario");
        inventario.setFechaActualizacion(LocalDateTime.now());
        return repo.save(inventario);
    }

    public Inventario actualizar(@NonNull Integer id, @NonNull Inventario inventario) {
        log.info("Actualizando registro de inventario ID: {}", id);
        Inventario existente = obtenerPorId(id);
        existente.setCantidad(inventario.getCantidad());
        existente.setFechaActualizacion(LocalDateTime.now());
        return repo.save(existente);
    }
}