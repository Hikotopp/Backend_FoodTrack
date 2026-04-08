package com.foodtrack.spring.springboot_application.controller;

// Importaciones necesarias
import com.foodtrack.spring.springboot_application.model.Mesa;
import com.foodtrack.spring.springboot_application.service.MesaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;  // Para evitar warnings de null safety
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/mesas")
@CrossOrigin(origins = "*")
public class MesaController {

    @Autowired
    private MesaService service;  // Servicio de mesas

    // Listar todas las mesas
    @GetMapping
    public List<Mesa> listar() {
        return service.listar();
    }

    // Obtener una mesa por ID
    @GetMapping("/{id}")
    public Mesa obtener(@PathVariable @NonNull Integer id) {  // @NonNull evita warning
        return service.obtenerPorId(id);
    }

    // Crear una nueva mesa
    @PostMapping
    public Mesa crear(@RequestBody @NonNull Mesa mesa) {  // @NonNull evita warning
        return service.guardar(mesa);
    }

    // Actualizar una mesa
    @PutMapping("/{id}")
    public Mesa actualizar(@PathVariable @NonNull Integer id, @RequestBody @NonNull Mesa mesa) {  // @NonNull evita warning
        return service.actualizar(id, mesa);
    }

    // Eliminar una mesa
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable @NonNull Integer id) {  // @NonNull evita warning
        service.eliminar(id);
    }

    // Cambiar estado de una mesa (LIBRE, OCUPADA, RESERVADA)
    @PatchMapping("/{id}/estado")
    public Mesa cambiarEstado(@PathVariable @NonNull Integer id, @RequestParam @NonNull String estado) {  // @NonNull evita warning
        return service.cambiarEstado(id, estado);
    }
}