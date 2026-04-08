package com.foodtrack.spring.springboot_application.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import com.foodtrack.spring.springboot_application.model.Producto;
import com.foodtrack.spring.springboot_application.repository.ProductoRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProductoService {

    @Autowired
    private ProductoRepository repo;

    public List<Producto> listar() {
        log.info("Listando todos los productos");
        return repo.findAll();
    }

    public Producto guardar(@NonNull Producto p) {
        log.info("Guardando producto: {}", p.getNombre());
        return repo.save(p);
    }

    public Producto actualizar(@NonNull Integer id, @NonNull Producto p) {
        log.info("Actualizando producto ID: {}", id);
        Producto existente = repo.findById(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));

        existente.setNombre(p.getNombre());
        existente.setPrecio(p.getPrecio());
        existente.setCategoria(p.getCategoria());
        existente.setDisponible(p.getDisponible());

        return repo.save(existente);
    }

    public void eliminar(@NonNull Integer id) {
        log.info("Eliminando producto ID: {}", id);
        if (!repo.existsById(id)) {
            throw new RuntimeException("Producto no encontrado con ID: " + id);
        }
        repo.deleteById(id);
    }

    public Producto obtenerPorId(@NonNull Integer id) {
        log.info("Buscando producto ID: {}", id);
        return repo.findById(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
    }
}