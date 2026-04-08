package com.foodtrack.spring.springboot_application.service;

import com.foodtrack.spring.springboot_application.model.Mesa;
import com.foodtrack.spring.springboot_application.repository.MesaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Slf4j
public class MesaService {

    @Autowired
    private MesaRepository repo;

    public List<Mesa> listar() {
        log.info("Listando todas las mesas");
        return repo.findAll();
    }

    public Mesa obtenerPorId(@NonNull Integer id) {
        log.info("Buscando mesa con ID: {}", id);
        return repo.findById(id)
            .orElseThrow(() -> new RuntimeException("Mesa no encontrada con ID: " + id));
    }

    public Mesa guardar(@NonNull Mesa mesa) {
        log.info("Guardando nueva mesa - Numero: {}", mesa.getNumero());
        return repo.save(mesa);
    }

    public Mesa actualizar(@NonNull Integer id, @NonNull Mesa mesa) {
        log.info("Actualizando mesa ID: {}", id);
        Mesa existente = obtenerPorId(id);
        existente.setNumero(mesa.getNumero());
        existente.setEstado(mesa.getEstado());
        return repo.save(existente);
    }

    public void eliminar(@NonNull Integer id) {
        log.info("Eliminando mesa ID: {}", id);
        repo.deleteById(id);
    }

    public Mesa cambiarEstado(@NonNull Integer id, @NonNull String estado) {
        log.info("Cambiando estado de mesa {} a {}", id, estado);
        Mesa mesa = obtenerPorId(id);
        mesa.setEstado(estado);
        return repo.save(mesa);
    }
}